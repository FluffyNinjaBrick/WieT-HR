package com.wiethr.app.model;

import com.wiethr.app.model.enums.EmployeeStatus;
import com.wiethr.app.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinTable(
            name="Permission_Owners",
            joinColumns = {@JoinColumn(name="permHolder")},
            inverseJoinColumns = {@JoinColumn(name="permSetID")}
    )
    //@JoinColumn(name = "permissions", referencedColumnName = "userEmail")
    private Permissions permissions;

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    // TODO - address as String??
    private String address;
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    private int yearsOfService;
    private int thisYearDaysOff;        // remaining days off from this year
    private int lastYearDaysOff;        // remaining days off carried over from last year


    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<AppreciationBonus> appreciationBonusList;



}
