package com.example.Energy_Dashboard.service;

import com.example.Energy_Dashboard.model.EnergyRecord;
import com.example.Energy_Dashboard.model.User;
import com.example.Energy_Dashboard.repository.EnergyRecordRepository;
import com.example.Energy_Dashboard.service.dto.AnalyticsSummary;
import com.example.Energy_Dashboard.service.dto.DailyUsage;
import com.example.Energy_Dashboard.service.dto.UsageAlert;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EnergyAnalyticsService {

    private final EnergyRecordRepository energyRecordRepository;
    private final double alertThresholdKwh;

    public EnergyAnalyticsService(EnergyRecordRepository energyRecordRepository,
                                  @Value("${energy.alert.threshold-kwh:50}") double alertThresholdKwh) {
        this.energyRecordRepository = energyRecordRepository;
        this.alertThresholdKwh = alertThresholdKwh;
    }

    public AnalyticsSummary buildSummary(User user, LocalDate now, YearMonth selectedMonth) {
        LocalDate weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);

        LocalDate monthStart = selectedMonth.atDay(1);
        LocalDate monthEnd = selectedMonth.atEndOfMonth();

        LocalDate yearStart = LocalDate.of(now.getYear(), 1, 1);
        LocalDate yearEnd = LocalDate.of(now.getYear(), 12, 31);

        RangeAverage weekly = averageForRange(user, weekStart, weekEnd);
        RangeAverage monthly = averageForRange(user, monthStart, monthEnd);
        RangeAverage yearly = averageForRange(user, yearStart, yearEnd);

        return new AnalyticsSummary(
                weekly.averageConsumption,
                weekly.averageGeneration,
                weekly.averageExport,
                monthly.averageConsumption,
                monthly.averageGeneration,
                monthly.averageExport,
                yearly.averageConsumption,
                yearly.averageGeneration,
                yearly.averageExport
        );
    }

    public List<DailyUsage> dailyUsageForMonth(User user, YearMonth selectedMonth) {
        LocalDate monthStart = selectedMonth.atDay(1);
        LocalDate monthEnd = selectedMonth.atEndOfMonth();
        List<EnergyRecord> records = energyRecordRepository.findByUserAndRecordDateBetween(user, monthStart, monthEnd);

        Map<LocalDate, DailyUsage> aggregated = new LinkedHashMap<>();
        records.stream()
                .sorted(Comparator.comparing(EnergyRecord::getRecordDate))
                .forEach(record -> {
                    DailyUsage existing = aggregated.get(record.getRecordDate());
                    if (existing == null) {
                        aggregated.put(record.getRecordDate(), new DailyUsage(
                                record.getRecordDate(),
                                record.getConsumptionKwh(),
                                record.getGenerationKwh(),
                                record.getExportToGridKwh()
                        ));
                    } else {
                        aggregated.put(record.getRecordDate(), new DailyUsage(
                                record.getRecordDate(),
                                existing.getConsumptionKwh() + record.getConsumptionKwh(),
                                existing.getGenerationKwh() + record.getGenerationKwh(),
                                existing.getExportToGridKwh() + record.getExportToGridKwh()
                        ));
                    }
                });

        return new ArrayList<>(aggregated.values());
    }

    public DailyUsage minConsumption(List<DailyUsage> usage) {
        return usage.stream()
                .min(Comparator.comparingDouble(DailyUsage::getConsumptionKwh))
                .orElse(null);
    }

    public DailyUsage maxConsumption(List<DailyUsage> usage) {
        return usage.stream()
                .max(Comparator.comparingDouble(DailyUsage::getConsumptionKwh))
                .orElse(null);
    }

    public DailyUsage minGeneration(List<DailyUsage> usage) {
        return usage.stream()
                .min(Comparator.comparingDouble(DailyUsage::getGenerationKwh))
                .orElse(null);
    }

    public DailyUsage maxGeneration(List<DailyUsage> usage) {
        return usage.stream()
                .max(Comparator.comparingDouble(DailyUsage::getGenerationKwh))
                .orElse(null);
    }

    public List<UsageAlert> buildAlerts(List<DailyUsage> usage) {
        return usage.stream()
                .filter(daily -> daily.getConsumptionKwh() > alertThresholdKwh)
                .map(daily -> new UsageAlert(daily.getDate(), daily.getConsumptionKwh()))
                .collect(Collectors.toList());
    }

    private RangeAverage averageForRange(User user, LocalDate start, LocalDate end) {
        List<EnergyRecord> records = energyRecordRepository.findByUserAndRecordDateBetween(user, start, end);
        if (records.isEmpty()) {
            return new RangeAverage(0, 0, 0);
        }
        double averageConsumption = records.stream().mapToDouble(EnergyRecord::getConsumptionKwh).average().orElse(0);
        double averageGeneration = records.stream().mapToDouble(EnergyRecord::getGenerationKwh).average().orElse(0);
        double averageExport = records.stream().mapToDouble(EnergyRecord::getExportToGridKwh).average().orElse(0);
        return new RangeAverage(averageConsumption, averageGeneration, averageExport);
    }

    private static class RangeAverage {
        private final double averageConsumption;
        private final double averageGeneration;
        private final double averageExport;

        private RangeAverage(double averageConsumption, double averageGeneration, double averageExport) {
            this.averageConsumption = averageConsumption;
            this.averageGeneration = averageGeneration;
            this.averageExport = averageExport;
        }
    }
}
