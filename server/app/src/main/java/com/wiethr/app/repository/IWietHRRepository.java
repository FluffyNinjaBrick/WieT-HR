package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(AddContractHelper helper);



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(DaysOffRequestHelper helper, String email);
    DaysOffRequest getDaysOffRequestByID(long documentID);
    void updateDaysOffRequest(DaysOffRequestHelper helper, Employee requestOwner);
    void removeDaysOffRequest(long documentID);
    List<DaysOffRequest> getAllDaysOffRequests();
    void signDaysOffRequest(long documentId, String email);


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(DelegationRequestHelper helper, String email);
    DelegationRequest getDelegationRequestByID(long documentID);
    void updateDelegationRequest(DelegationRequestHelper helper, Employee requestOwner);
    void removeDelegationRequest(long documentID);
    List<DelegationRequest> getAllDelegationRequests();
    void signDelegationRequest(long documentId, String email);

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
    void addAppreciationBonus(AddAppreciationBonusHelper bonusHelper);

    // ---------- PERMISSIONS ----------
    Permissions createPermissionsFromHelper(PermissionHelper helper);

    // ---------- BONUS BUDGET ----------
    BonusBudget getBonusBudgetForYear(Year year);
    float getBonusBudgetLeft(BonusBudget bonusBudget);
    List<Float> getBonusBudgetUsagePerMonth(BonusBudget bonusBudget);
    BonusesOfAllEmployeesHelper getBonusesForYear(Year year);

}
