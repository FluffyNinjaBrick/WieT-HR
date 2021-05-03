package com.wiethr.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class AppreciationBonus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ManyToOne
    @JoinColumn(name = "employee", referencedColumnName = "id", nullable = false)
    private Employee employee;

    private YearMonth yearMonth;
    private Date dateGenerated;
    private Float value;

    @ManyToOne
    @JoinColumn(name = "bonusBugdet", referencedColumnName = "id", nullable = false)
    private BonusBudget bonusBudget;

    public AppreciationBonus(Employee employee ,YearMonth yearMonth, Date dateGenerated, Float value, BonusBudget bonusBudget){
        this.employee = employee;
        this.yearMonth = yearMonth;
        this.dateGenerated = dateGenerated;
        this.value = value;
        this.bonusBudget = bonusBudget;
    }

//    public long getEmployee() {
//        return this.employee.getId();
//    }
//
//    public Employee employeeObject() {
//        return this.employee;
//    }

}
