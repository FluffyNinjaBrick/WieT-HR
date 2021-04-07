package com.wiethr.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
//@SequenceGenerator(name = "permGen", initialValue = 3)
public class Permissions {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permGen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="Managed_Users",
            joinColumns = {@JoinColumn(name="permHolder")},
            inverseJoinColumns = {@JoinColumn(name="managedUser")}
    )
    private List<Employee> managedUsers;

    private boolean addUsers;
    private boolean modifyBonusBudget;


    public Permissions(boolean addUsers, boolean modifyBonusBudget) {
        this.addUsers = addUsers;
        this.modifyBonusBudget = modifyBonusBudget;
        this.managedUsers = new ArrayList<>();
    }

}
