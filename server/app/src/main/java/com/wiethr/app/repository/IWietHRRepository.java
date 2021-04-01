package com.wiethr.app.repository;

import com.wiethr.app.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------



    // ---------- DAYS OFF REQUEST ----------



    // ---------- DELEGATION REQUEST ----------



    // ---------- EMPLOYEE ----------
    List<Employee> getAllEmployees();

    Optional<Employee> getEmployee(long id);

    Employee updateOrAddEmployee(Employee employee);



    // ---------- PERMISSIONS ----------


}
