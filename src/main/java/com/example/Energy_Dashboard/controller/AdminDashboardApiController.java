package com.example.Energy_Dashboard.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Energy_Dashboard.model.User;
import com.example.Energy_Dashboard.repository.EnergyRecordRepository;
import com.example.Energy_Dashboard.repository.UserRepository;
import com.example.Energy_Dashboard.service.EnergyAnalyticsService;
import com.example.Energy_Dashboard.service.dto.AdminDashboardResponse;
import com.example.Energy_Dashboard.service.dto.AnalyticsSummary;
import com.example.Energy_Dashboard.service.dto.DailyUsage;
import com.example.Energy_Dashboard.service.dto.UsageAlert;
import com.example.Energy_Dashboard.service.dto.UserSummary;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*" , allowedHeaders = "*")
public class AdminDashboardApiController {

    private final UserRepository userRepository;
    private final EnergyAnalyticsService analyticsService;
    private final EnergyRecordRepository energyRecordRepository;

    public AdminDashboardApiController(UserRepository userRepository, EnergyAnalyticsService analyticsService,
                                      EnergyRecordRepository energyRecordRepository) {
        this.userRepository = userRepository;
        this.analyticsService = analyticsService;
        this.energyRecordRepository = energyRecordRepository;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard(@RequestParam(name = "userId", required = false) Long userId,
                                            @RequestParam(name = "month", required = false) Integer month,
                                            @RequestParam(name = "year", required = false) Integer year) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new AdminDashboardResponse(List.of(), null, 0, 0, "", null, List.of(), List.of(), null, null, null, null,
                    List.of(), List.of(), List.of(), List.of());
        }

        User selectedUser = resolveUser(users, userId);
        LocalDate now = LocalDate.now();
        int selectedYear = Optional.ofNullable(year).orElse(now.getYear());
        int selectedMonthNumber = Optional.ofNullable(month).orElse(now.getMonthValue());
        YearMonth selectedYearMonth = YearMonth.of(selectedYear, selectedMonthNumber);

        AnalyticsSummary summary = analyticsService.buildSummary(selectedUser, now, selectedYearMonth);
        List<DailyUsage> dailyUsage = analyticsService.dailyUsageForMonth(selectedUser, selectedYearMonth);
        List<UsageAlert> alerts = analyticsService.buildAlerts(dailyUsage);

        DailyUsage minConsumption = analyticsService.minConsumption(dailyUsage);
        DailyUsage maxConsumption = analyticsService.maxConsumption(dailyUsage);
        DailyUsage minGeneration = analyticsService.minGeneration(dailyUsage);
        DailyUsage maxGeneration = analyticsService.maxGeneration(dailyUsage);

        List<UserSummary> userSummaries = users.stream()
                .map(user -> new UserSummary(user.getId(), user.getUsername(), user.getEmailId(), user.getRole()))
                .toList();

        UserSummary selectedSummary = new UserSummary(selectedUser.getId(), selectedUser.getUsername(), selectedUser.getEmailId(), selectedUser.getRole());

        List<String> dailyLabels = dailyUsage.stream()
                .map(d -> d.getDate().format(DateTimeFormatter.ISO_DATE))
                .toList();
        List<Double> dailyConsumption = dailyUsage.stream().map(DailyUsage::getConsumptionKwh).toList();
        List<Double> dailyGeneration = dailyUsage.stream().map(DailyUsage::getGenerationKwh).toList();
        List<Double> dailyExport = dailyUsage.stream().map(DailyUsage::getExportToGridKwh).toList();

        return new AdminDashboardResponse(
                userSummaries,
                selectedSummary,
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

    private User resolveUser(List<User> users, Long userId) {
        if (userId == null) {
            return users.getFirst();
        }
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseGet(users::getFirst);
    }

    @GetMapping("/available-years")
    public List<Integer> getAvailableYears(@RequestParam(name = "userId", required = false) Long userId) {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return List.of(LocalDate.now().getYear());
        }

        User selectedUser = resolveUser(users, userId);
        List<Integer> years = energyRecordRepository.findAvailableYearsForUser(selectedUser);
        
        // If no years found, return current year as default
        if (years.isEmpty()) {
            years = List.of(LocalDate.now().getYear());
        }
        
        return years;
    }
}
