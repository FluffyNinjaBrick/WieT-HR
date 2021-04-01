package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDaysOffRequestHelper {

    private long employeeID;
    private Date dateFrom, dateTo;
    private LeaveType leaveType;

}
