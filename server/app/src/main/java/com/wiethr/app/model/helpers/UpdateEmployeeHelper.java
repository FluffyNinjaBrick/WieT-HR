package com.wiethr.app.model.helpers;

import com.wiethr.app.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployeeHelper {

    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private UserRole role;

}
