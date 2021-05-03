package com.wiethr.app.repository;

import com.wiethr.app.model.Contract;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.DelegationRequest;
import com.wiethr.app.model.Employee;
import com.wiethr.app.model.helpers.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(Contract contract);



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(DaysOffRequest daysOffRequest);
    DaysOffRequest getDaysOffRequestByID(long documentID);
    void updateDaysOffRequest(long documentID, AddDaysOffRequestHelper addDaysOffRequestHelper);
    void removeDaysOffRequest(long documentID);
    List<DaysOffRequest> getAllDaysOffRequests();


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(DelegationRequest delegationRequest);
    DelegationRequest getDelegationRequestByID(long documentID);
    void updateDelegationRequest(long documentID, AddDelegationRequestHelper delegationRequestHelper);
    void removeDelegationRequest(long documentID);
    List<DelegationRequest> getAllDelegationRequests();

    // ---------- EMPLOYEE ----------
    List<Employee> getAllEmployees();
    Employee getEmployee(long id);
    Employee getEmployeeByEmail(String email);
    Employee updateEmployee(Employee employee);
    void createEmployee(AddEmployeeHelper helper);
    void removeEmployee(long id);
    List<AbsentEmployees> getAbsentEmployees(LocalDate from, LocalDate to);

    List<DelegationRequest> getEmployeeDelegationRequests(long id, LocalDate from, LocalDate to);
    List<DaysOffRequest> getEmployeeDaysOffRequests(long id, LocalDate from, LocalDate to);

    EmployeeDaysOffDetails getEmployeeDaysOffLeft(long id);
    GroupDaysOffDetails getGroupDaysOffLeft(String email);


    // ---------- PERMISSIONS ----------


}
