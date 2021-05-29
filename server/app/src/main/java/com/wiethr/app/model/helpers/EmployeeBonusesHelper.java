package com.wiethr.app.model.helpers;

import com.wiethr.app.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Year;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeBonusesHelper {
    private long employeeId;
    private List<EmployeeBonusesPerYear> bonuses;

    @Data
    public static class EmployeeBonusesPerYear {
        private Year year;
        private float[] monthlyBonus;

        public EmployeeBonusesPerYear(int year) {
            this.year = Year.of(year);
            this.monthlyBonus = new float[12];
            Arrays.fill(monthlyBonus, 0.0f);
        }

        public void addBonusToMonth(int month, float value) {
            this.monthlyBonus[month - 1] += value;
        }
    }
}

