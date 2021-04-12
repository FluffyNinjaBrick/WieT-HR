package com.wiethr.app.model.helpers;

import lombok.Data;
import java.util.List;

@Data
public class PermissionHelper{
    private List<Long> managedUsers;
    private boolean addUsers;
    private boolean modifyBonusBudget;
}
