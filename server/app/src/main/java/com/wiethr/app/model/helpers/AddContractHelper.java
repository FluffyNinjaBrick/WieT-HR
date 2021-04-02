package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddContractHelper {

    private long employeeID;
    private LocalDate dateFrom, dateTo;
    private float salary;
    private float dutyAllowance;
    private int workingHours;
    private int annualLeaveDays;
    private ContractType type;

}
