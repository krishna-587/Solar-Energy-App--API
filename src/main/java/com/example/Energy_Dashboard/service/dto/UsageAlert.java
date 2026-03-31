package com.example.Energy_Dashboard.service.dto;

import java.time.LocalDate;

public class UsageAlert {
    private final LocalDate date;
    private final double consumptionKwh;

    public UsageAlert(LocalDate date, double consumptionKwh) {
        this.date = date;
        this.consumptionKwh = consumptionKwh;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getConsumptionKwh() {
        return consumptionKwh;
    }
}
