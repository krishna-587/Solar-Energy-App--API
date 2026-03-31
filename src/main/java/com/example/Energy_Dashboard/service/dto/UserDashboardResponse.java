package com.example.Energy_Dashboard.service.dto;

import java.util.List;

public class UserDashboardResponse {
    private final Long userId;
    private final String username;
    private final String emailId;
    private final int selectedMonth;
    private final int selectedYear;
    private final String monthLabel;
    private final AnalyticsSummary summary;
    private final List<DailyUsage> dailyUsage;
    private final List<UsageAlert> alerts;
    private final DailyUsage minConsumption;
    private final DailyUsage maxConsumption;
    private final DailyUsage minGeneration;
    private final DailyUsage maxGeneration;
    private final List<String> dailyLabels;
    private final List<Double> dailyConsumption;
    private final List<Double> dailyGeneration;
    private final List<Double> dailyExport;

    public UserDashboardResponse(Long userId,
                                 String username,
                                 String emailId,
                                 int selectedMonth,
                                 int selectedYear,
                                 String monthLabel,
                                 AnalyticsSummary summary,
                                 List<DailyUsage> dailyUsage,
                                 List<UsageAlert> alerts,
                                 DailyUsage minConsumption,
                                 DailyUsage maxConsumption,
                                 DailyUsage minGeneration,
                                 DailyUsage maxGeneration,
                                 List<String> dailyLabels,
                                 List<Double> dailyConsumption,
                                 List<Double> dailyGeneration,
                                 List<Double> dailyExport) {
        this.userId = userId;
        this.username = username;
        this.emailId = emailId;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        this.monthLabel = monthLabel;
        this.summary = summary;
        this.dailyUsage = dailyUsage;
        this.alerts = alerts;
        this.minConsumption = minConsumption;
        this.maxConsumption = maxConsumption;
        this.minGeneration = minGeneration;
        this.maxGeneration = maxGeneration;
        this.dailyLabels = dailyLabels;
        this.dailyConsumption = dailyConsumption;
        this.dailyGeneration = dailyGeneration;
        this.dailyExport = dailyExport;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailId() {
        return emailId;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    public AnalyticsSummary getSummary() {
        return summary;
    }

    public List<DailyUsage> getDailyUsage() {
        return dailyUsage;
    }

    public List<UsageAlert> getAlerts() {
        return alerts;
    }

    public DailyUsage getMinConsumption() {
        return minConsumption;
    }

    public DailyUsage getMaxConsumption() {
        return maxConsumption;
    }

    public DailyUsage getMinGeneration() {
        return minGeneration;
    }

    public DailyUsage getMaxGeneration() {
        return maxGeneration;
    }

    public List<String> getDailyLabels() {
        return dailyLabels;
    }

    public List<Double> getDailyConsumption() {
        return dailyConsumption;
    }

    public List<Double> getDailyGeneration() {
        return dailyGeneration;
    }

    public List<Double> getDailyExport() {
        return dailyExport;
    }
}
