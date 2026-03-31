package com.example.Energy_Dashboard.service.dto;

import java.time.LocalDate;

public class DailyUsage {
    private final LocalDate date;
    private final double consumptionKwh;
    private final double generationKwh;
    private final double exportToGridKwh;

    public DailyUsage(LocalDate date, double consumptionKwh, double generationKwh, double exportToGridKwh) {
        this.date = date;
        this.consumptionKwh = consumptionKwh;
        this.generationKwh = generationKwh;
        this.exportToGridKwh = exportToGridKwh;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getConsumptionKwh() {
        return consumptionKwh;
    }

    public double getGenerationKwh() {
        return generationKwh;
    }

    public double getExportToGridKwh() {
        return exportToGridKwh;
    }
}
