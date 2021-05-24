package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAnnexHelper {

    private long employeeId;
    private long contractId;
    private LocalDate dateFrom;
    @Nullable private LocalDate dateTo;
    private float salary;
    private float dutyAllowance;
    private int workingHours;
    private int annualLeaveDays;
    private ContractType type;
}
