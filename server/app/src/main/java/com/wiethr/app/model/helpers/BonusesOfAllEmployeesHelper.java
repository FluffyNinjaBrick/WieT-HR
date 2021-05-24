package com.wiethr.app.model.helpers;

import lombok.Data;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
public class BonusesOfAllEmployeesHelper {

    private long bonusBudgetId;
    private Year year;
    private float budgetLeft;
    private List<Float> monthlySummary;
    private List<BonusesOfEmployeeHelper> bonuses;

    public BonusesOfAllEmployeesHelper(long bonusBudgetId, Year year, float budgetLeft) {
        this.bonusBudgetId = bonusBudgetId;
        this.year = year;
        this.budgetLeft = budgetLeft;
        this.monthlySummary = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            monthlySummary.add(0.0f);
        }
        this.bonuses = new ArrayList<>();
    }

    public void useBudget(float value) {
        this.budgetLeft -= value;
    }
}
