package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDelegationRequestHelper {

    private long employeeID;
    private LocalDate dateFrom, dateTo;
    private String destination;

}
