package com.wiethr.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
public class Permissions {

    @Id
    private long userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="Managed_Users",
            joinColumns = {@JoinColumn(name="permHolder")},
            inverseJoinColumns = {@JoinColumn(name="managedUser")}
    )
    private List<Employee> managedUsers;

    private boolean addUsers;
    private boolean modifyBonusBudget;


    public Permissions(long userId, boolean addUsers, boolean modifyBonusBudget) {
        this.userId = userId;
        this.addUsers = addUsers;
        this.modifyBonusBudget = modifyBonusBudget;
        this.managedUsers = new ArrayList<>();
    }

}
