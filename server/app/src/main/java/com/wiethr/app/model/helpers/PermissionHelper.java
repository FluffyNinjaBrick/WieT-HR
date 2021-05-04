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

    private List<Long> managedUsers;
    private boolean addUsers;
    private boolean modifyBonusBudget;

    PermissionHelper(Permissions permissions) {
        this.addUsers = permissions.isAddUsers();
        this.modifyBonusBudget = permissions.isModifyBonusBudget();

        this.managedUsers = new ArrayList<>();
        for (Employee e: permissions.managedUsersObject()) {
            this.managedUsers.add(e.getId());
        }
    }

}
