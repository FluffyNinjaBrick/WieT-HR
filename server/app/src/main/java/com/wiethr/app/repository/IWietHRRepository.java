package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(AddContractHelper helper);



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(AddDaysOffRequestHelper helper);
    DaysOffRequest getDaysOffRequestByID(long documentID);
    void updateDaysOffRequest(long documentID, AddDaysOffRequestHelper addDaysOffRequestHelper);
    void removeDaysOffRequest(long documentID);
    List<DaysOffRequest> getAllDaysOffRequests();


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(AddDelegationRequestHelper helper);
    DelegationRequest getDelegationRequestByID(long documentID);
    void updateDelegationRequest(long documentID, AddDelegationRequestHelper delegationRequestHelper);
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


    // ---------- PERMISSIONS ----------
    Permissions createPermissionsFromHelper(PermissionHelper helper);

}
