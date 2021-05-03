package com.wiethr.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@Data
//@SequenceGenerator(name = "permGen", initialValue = 3)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    public void addManagedUser(Employee user) {
        this.managedUsers.add(user);
    }

    public List<Long> getManagedUsers() {
        return this.managedUsers.stream().map(Employee::getId).collect(Collectors.toList());
    }

    public List<Employee> managedUsersObject() {
        return this.managedUsers;
    }

}
