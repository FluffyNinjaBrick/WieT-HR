package com.wiethr.app.model.helpers;

import lombok.Data;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Date;

@Data
public class AddAppreciationBonusHelper {

    private long employeeId;
    private YearMonth yearMonth;
    private LocalDate dateGenerated;
    private float value;
    private long bonusBudgetId;
}
