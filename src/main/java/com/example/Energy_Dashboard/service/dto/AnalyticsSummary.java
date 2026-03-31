package com.example.Energy_Dashboard.service.dto;

public class AnalyticsSummary {
    private final double weeklyAverageConsumption;
    private final double weeklyAverageGeneration;
    private final double weeklyAverageExport;
    private final double monthlyAverageConsumption;
    private final double monthlyAverageGeneration;
    private final double monthlyAverageExport;
    private final double yearlyAverageConsumption;
    private final double yearlyAverageGeneration;
    private final double yearlyAverageExport;

    public AnalyticsSummary(double weeklyAverageConsumption,
                            double weeklyAverageGeneration,
                            double weeklyAverageExport,
                            double monthlyAverageConsumption,
                            double monthlyAverageGeneration,
                            double monthlyAverageExport,
                            double yearlyAverageConsumption,
                            double yearlyAverageGeneration,
                            double yearlyAverageExport) {
        this.weeklyAverageConsumption = weeklyAverageConsumption;
        this.weeklyAverageGeneration = weeklyAverageGeneration;
        this.weeklyAverageExport = weeklyAverageExport;
        this.monthlyAverageConsumption = monthlyAverageConsumption;
        this.monthlyAverageGeneration = monthlyAverageGeneration;
        this.monthlyAverageExport = monthlyAverageExport;
        this.yearlyAverageConsumption = yearlyAverageConsumption;
        this.yearlyAverageGeneration = yearlyAverageGeneration;
        this.yearlyAverageExport = yearlyAverageExport;
    }

    public double getWeeklyAverageConsumption() {
        return weeklyAverageConsumption;
    }

    public double getWeeklyAverageGeneration() {
        return weeklyAverageGeneration;
    }

    public double getWeeklyAverageExport() {
        return weeklyAverageExport;
    }

    public double getMonthlyAverageConsumption() {
        return monthlyAverageConsumption;
    }

    public double getMonthlyAverageGeneration() {
        return monthlyAverageGeneration;
    }

    public double getMonthlyAverageExport() {
        return monthlyAverageExport;
    }

    public double getYearlyAverageConsumption() {
        return yearlyAverageConsumption;
    }

    public double getYearlyAverageGeneration() {
        return yearlyAverageGeneration;
    }

    public double getYearlyAverageExport() {
        return yearlyAverageExport;
    }
}
