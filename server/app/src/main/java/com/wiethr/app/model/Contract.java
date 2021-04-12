package com.wiethr.app.model;

import com.wiethr.app.model.enums.ContractType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

}