package com.wiethr.app.model.helpers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployeeHelper {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

}
