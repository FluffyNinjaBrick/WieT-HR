package com.wiethr.app.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbsentEmployees {

    LocalDate day;
    List<String> absentEmployees = new ArrayList<>();

    public AbsentEmployees(LocalDate day) {
        this.day = day;
        this.absentEmployees = new ArrayList<>();
    }

    public void addEmployee(String name) {
        this.absentEmployees.add(name);
    }

}
