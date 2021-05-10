package com.wiethr.app.model.helpers;

import lombok.Data;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
public class BonusesOfAllEmployeesHelper {

    private Year year;
    private List<Float> monthlySummary;
    private List<BonusesOfEmployeeHelper> bonuses;

    public BonusesOfAllEmployeesHelper(Year year) {
        this.year = year;
        this.monthlySummary = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            monthlySummary.add(0.0f);
        }
        this.bonuses = new ArrayList<>();
    }
}
