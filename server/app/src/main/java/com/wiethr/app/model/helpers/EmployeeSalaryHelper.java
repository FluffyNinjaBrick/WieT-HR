package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeSalaryHelper {
    private long employeeId;
    private String employeeName;
    private float[] monthlySum;
    private float sum;

    public void increaseSum(float value) {
        this.sum += value;
    }
}
