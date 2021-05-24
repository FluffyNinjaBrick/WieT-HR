package com.wiethr.app.model;

import com.wiethr.app.model.enums.ContractType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Contract extends Document {

    private float salary;
    private float dutyAllowance;
    private int workingHours;
    private int annualLeaveDays;

    @Enumerated(EnumType.STRING)
    private ContractType type;

    @OneToMany(orphanRemoval = true)
    @JoinTable(
            name="Contract_Annexes",
            joinColumns = {@JoinColumn(name="originals")},
            inverseJoinColumns = {@JoinColumn(name="annexes")}
    )
    private List<Contract> annexes;

    public Contract(Employee employee, LocalDate dateFrom, LocalDate dateTo, float salary, float dutyAllowance, int workingHours, int annualLeaveDays, ContractType type, List<Contract> annexes) {
        super(employee, LocalDate.now(), dateFrom, dateTo);
        this.salary = salary;
        this.dutyAllowance = dutyAllowance;
        this.workingHours = workingHours;
        this.annualLeaveDays = annualLeaveDays;
        this.type = type;
        this.annexes = annexes;
    }

    public void addAnnex(Contract annex) {
        this.annexes.add(annex);
    }
}
