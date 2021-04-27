package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDaysOffDetails {

    long employeeID;
    String employeeName;
    int daysOffUsed;
    int daysOffLeft;

}
