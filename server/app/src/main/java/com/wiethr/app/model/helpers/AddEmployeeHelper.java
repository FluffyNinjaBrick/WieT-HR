package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.EmployeeStatus;
import com.wiethr.app.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class AddEmployeeHelper {

    private PermissionHelper permissionHelper;

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String address;
    private String phone;

    private UserRole userRole;
    private EmployeeStatus status;

    private int yearsOfService;
    private int thisYearDaysOff;        // remaining days off from this year
    private int lastYearDaysOff;        // remaining days off carried over from last year

}
