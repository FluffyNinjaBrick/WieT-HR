package com.wiethr.app.repository;

import com.wiethr.app.model.Contract;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.DelegationRequest;
import com.wiethr.app.model.Employee;

import java.util.List;
import java.util.Optional;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(Contract contract);



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(DaysOffRequest daysOffRequest);


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(DelegationRequest delegationRequest);


    // ---------- EMPLOYEE ----------
    List<Employee> getAllEmployees();

    Optional<Employee> getEmployee(long id);

    Employee updateOrAddEmployee(Employee employee);



    // ---------- PERMISSIONS ----------


}
