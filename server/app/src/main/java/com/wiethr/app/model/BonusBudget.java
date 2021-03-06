package com.wiethr.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class BonusBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Year year;
    private Float value;

    @OneToMany(mappedBy = "bonusBudget", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AppreciationBonus> bonusList;

    public BonusBudget(Year year, Float value){
        this.year = year;
        this.value = value;
        bonusList = new ArrayList<>();
    }

}
