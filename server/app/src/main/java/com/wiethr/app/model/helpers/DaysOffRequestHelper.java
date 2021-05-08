package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaysOffRequestHelper {

    private long documentId;
    private LocalDate dateFrom, dateTo;
    private LeaveType leaveType;

}
