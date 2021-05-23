package com.wiethr.app.model.helpers;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmployeesSalariesHelper {

    private float[] monthlySum;
    private Map<Long, float[]> monthlySumPerEmployee;

    public EmployeesSalariesHelper(float[] monthlySum, Map<Long, float[]> monthlySumPerEmployee) {
        this.monthlySum = monthlySum;
        this.monthlySumPerEmployee = monthlySumPerEmployee;
    }
}
