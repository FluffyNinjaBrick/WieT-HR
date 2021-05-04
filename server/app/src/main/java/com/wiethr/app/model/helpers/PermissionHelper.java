package com.wiethr.app.model.helpers;

import com.wiethr.app.model.Employee;
import com.wiethr.app.model.Permissions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PermissionHelper {

    private long employeeId;
    private List<Long> managedUsers;
    private boolean addUsers;
    private boolean modifyBonusBudget;

    PermissionHelper(Permissions permissions) {

        // this is the ID of the permission set, but it's identical to the owner's ID
        this.employeeId = permissions.getId();
        this.addUsers = permissions.isAddUsers();
        this.modifyBonusBudget = permissions.isModifyBonusBudget();

        this.managedUsers = new ArrayList<>();
        for (Employee e: permissions.managedUsersObject()) {
            this.managedUsers.add(e.getId());
        }
    }

}
