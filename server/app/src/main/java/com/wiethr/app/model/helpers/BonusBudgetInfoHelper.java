package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
public class BonusBudgetInfoHelper {

    private long budgetId;
    private Year year;
    private float budgetSize;
    private float budgetLeft;
    private List<Float> budgetUsagePerMonth;

}
