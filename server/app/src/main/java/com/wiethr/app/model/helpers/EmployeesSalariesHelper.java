package com.wiethr.app.model.helpers;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmployeesSalariesHelper {

    private float[] monthlySum;
//    private Map<Long, EmployeeSalaryHelper> monthlySumPerEmployee;
    private List<EmployeeSalaryHelper> monthlySumPerEmployee;

    public EmployeesSalariesHelper(float[] monthlySum, List<EmployeeSalaryHelper> monthlySumPerEmployee) {
        this.monthlySum = monthlySum;
        this.monthlySumPerEmployee = monthlySumPerEmployee;
    }
}
