package com.wiethr.app.model.helpers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BonusesOfEmployeeHelper {

    private long employeeId;
    private String employeeName;
    private List<Float> employeeBonuses;
    private float employeeBonusesTotal;

    public BonusesOfEmployeeHelper(long employeeId, String employeeName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeBonuses = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            employeeBonuses.add(0.0f);
        }
        this.employeeBonusesTotal = 0.0f;
    }

    public void addBonusToTotal(float value) {
        this.employeeBonusesTotal += value;
    }
}
