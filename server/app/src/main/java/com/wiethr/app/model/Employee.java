package com.wiethr.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wiethr.app.model.enums.EmployeeStatus;
import com.wiethr.app.model.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
//@SequenceGenerator(name = "empGen", initialValue = 3)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empGen")
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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


    @JsonIgnore
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<AppreciationBonus> appreciationBonusList;

    public Employee(long id, String email, String firstName, String lastName, String address, String phone) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public void reduceDaysOffRequestLeft(int newDaysOffRequestDuration){
        int daysLeft = this.lastYearDaysOff + this.thisYearDaysOff;

        if(daysLeft - newDaysOffRequestDuration < 0 ){
            throw new IllegalArgumentException("Error: the employee has too few days off left");
        }

        if(this.lastYearDaysOff > 0){
            if(this.lastYearDaysOff >= newDaysOffRequestDuration){
                this.lastYearDaysOff -= newDaysOffRequestDuration;
                return;
            } else {
                newDaysOffRequestDuration -= this.lastYearDaysOff;
                this.lastYearDaysOff = 0;
            }
        }

        this.thisYearDaysOff -= newDaysOffRequestDuration;
    }
}
