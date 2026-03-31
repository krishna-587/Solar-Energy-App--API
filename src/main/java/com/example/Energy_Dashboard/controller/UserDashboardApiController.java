package com.example.Energy_Dashboard.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.Energy_Dashboard.model.User;
import com.example.Energy_Dashboard.repository.EnergyRecordRepository;
import com.example.Energy_Dashboard.repository.UserRepository;
import com.example.Energy_Dashboard.service.EnergyAnalyticsService;
import com.example.Energy_Dashboard.service.dto.AnalyticsSummary;
import com.example.Energy_Dashboard.service.dto.DailyUsage;
import com.example.Energy_Dashboard.service.dto.UsageAlert;
import com.example.Energy_Dashboard.service.dto.UserDashboardResponse;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class UserDashboardApiController {

    private final UserRepository userRepository;
    private final EnergyAnalyticsService analyticsService;
    private final EnergyRecordRepository energyRecordRepository;

    public UserDashboardApiController(UserRepository userRepository, EnergyAnalyticsService analyticsService,
                                     EnergyRecordRepository energyRecordRepository) {
        this.userRepository = userRepository;
        this.analyticsService = analyticsService;
        this.energyRecordRepository = energyRecordRepository;
    }

    @GetMapping("/dashboard")
    public UserDashboardResponse dashboard(@RequestParam(name = "month", required = false) Integer month,
                                           @RequestParam(name = "year", required = false) Integer year,
                                           Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDate now = LocalDate.now();
        int selectedYear = Optional.ofNullable(year).orElse(now.getYear());
        int selectedMonthNumber = Optional.ofNullable(month).orElse(now.getMonthValue());
        YearMonth selectedYearMonth = YearMonth.of(selectedYear, selectedMonthNumber);

        AnalyticsSummary summary = analyticsService.buildSummary(user, now, selectedYearMonth);
        List<DailyUsage> dailyUsage = analyticsService.dailyUsageForMonth(user, selectedYearMonth);
        List<UsageAlert> alerts = analyticsService.buildAlerts(dailyUsage);

        DailyUsage minConsumption = analyticsService.minConsumption(dailyUsage);
        DailyUsage maxConsumption = analyticsService.maxConsumption(dailyUsage);
        DailyUsage minGeneration = analyticsService.minGeneration(dailyUsage);
        DailyUsage maxGeneration = analyticsService.maxGeneration(dailyUsage);

        List<String> dailyLabels = dailyUsage.stream()
                .map(d -> d.getDate().format(DateTimeFormatter.ISO_DATE))
                .toList();
        List<Double> dailyConsumption = dailyUsage.stream().map(DailyUsage::getConsumptionKwh).toList();
        List<Double> dailyGeneration = dailyUsage.stream().map(DailyUsage::getGenerationKwh).toList();
        List<Double> dailyExport = dailyUsage.stream().map(DailyUsage::getExportToGridKwh).toList();

        return new UserDashboardResponse(
                user.getId(),
                user.getUsername(),
                user.getEmailId(),
                selectedMonthNumber,
                selectedYear,
                selectedYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                summary,
                dailyUsage,
                alerts,
                minConsumption,
                maxConsumption,
                minGeneration,
                maxGeneration,
                dailyLabels,
                dailyConsumption,
                dailyGeneration,
                dailyExport
        );
    }

    @GetMapping("/available-years")
    public List<Integer> getAvailableYears(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Integer> years = energyRecordRepository.findAvailableYearsForUser(user);
        
        // If no years found, return current year as default
        if (years.isEmpty()) {
            years = List.of(LocalDate.now().getYear());
        }
        
        return years;
    }
}
