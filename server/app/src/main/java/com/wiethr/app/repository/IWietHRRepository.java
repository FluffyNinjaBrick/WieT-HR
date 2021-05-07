package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;

import java.time.LocalDate;
import java.util.List;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(AddContractHelper helper);



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(DaysOffRequestHelper helper);
    DaysOffRequest getDaysOffRequestByID(long documentID);
    void updateDaysOffRequest(DaysOffRequestHelper daysOffRequestHelper);
    void removeDaysOffRequest(long documentID);
    List<DaysOffRequest> getAllDaysOffRequests();


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(DelegationRequestHelper helper);
    DelegationRequest getDelegationRequestByID(long documentID);
    void updateDelegationRequest(DelegationRequestHelper delegationRequestHelper);
    void removeDelegationRequest(long documentID);
    List<DelegationRequest> getAllDelegationRequests();

    // ---------- EMPLOYEE ----------
    List<Employee> getAllEmployees();
    Employee getEmployee(long id);
    Employee getEmployeeByEmail(String email);
    void updateEmployee(UpdateEmployeeHelper helper);
    void updateEmployeePermissions(PermissionHelper helper);
    void createEmployee(AddEmployeeHelper helper);
    void removeEmployee(long id);
    List<AbsentEmployees> getAbsentEmployees(LocalDate from, LocalDate to);

    List<DelegationRequest> getEmployeeDelegationRequests(long id, LocalDate from, LocalDate to);
    List<DaysOffRequest> getEmployeeDaysOffRequests(long id, LocalDate from, LocalDate to);

    EmployeeDaysOffDetails getEmployeeDaysOffLeft(long id);
    GroupDaysOffDetails getGroupDaysOffLeft(String email);

    List<AppreciationBonus> getEmployeeBonuses(long id);

    // ---------- PERMISSIONS ----------
    Permissions createPermissionsFromHelper(PermissionHelper helper);

}
