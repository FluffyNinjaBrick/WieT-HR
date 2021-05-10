package com.wiethr.app.security;

import com.wiethr.app.model.Employee;
import com.wiethr.app.model.enums.UserRole;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    private final WietHRRepository repository;

    @Autowired
    public RoleValidator(WietHRRepository repository) {
        this.repository = repository;
    }


    public void validate(String requestingEmail, long subordinateId) throws IllegalAccessException {

        Employee requesting = this.repository.getEmployeeByEmail(requestingEmail);

        // case 1 - employee
        if (requesting.getUserRole() == UserRole.EMPLOYEE) {
            if (requesting.getId() == subordinateId) return;
        }
        // case 2 - manager
        else if (requesting.getUserRole() == UserRole.MANAGER) {
            for (Employee subordinate: requesting.getPermissions().managedUsersObject())
                if (subordinate.getId() == subordinateId) return;
        }
        // case 3 - admin
        else if (requesting.getUserRole() == UserRole.ADMIN) {
            return;
        }
        throw new IllegalAccessException("Error: you do not have permission to access this resource");
    }

    public void validateAbsent(String requestingEmail) throws IllegalAccessException {
        Employee requesting = this.repository.getEmployeeByEmail(requestingEmail);
        if(!(requesting.getUserRole() == UserRole.MANAGER || requesting.getUserRole() == UserRole.ADMIN))
            throw new IllegalAccessException("Error: you do not have permission to access this resource"); ;
    }

}
