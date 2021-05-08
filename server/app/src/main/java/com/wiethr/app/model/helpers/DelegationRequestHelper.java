package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelegationRequestHelper {

    private long documentId;
    private LocalDate dateFrom, dateTo;
    private String destination;

}
