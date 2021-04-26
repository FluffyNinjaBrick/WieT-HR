package com.wiethr.app.repository;

import com.wiethr.app.model.Contract;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.DelegationRequest;
import com.wiethr.app.model.Employee;
import com.wiethr.app.model.helpers.AbsentEmployees;
import com.wiethr.app.model.helpers.AddDaysOffRequestHelper;
import com.wiethr.app.model.helpers.AddDelegationRequestHelper;
import com.wiethr.app.model.helpers.AddEmployeeHelper;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IWietHRRepository {

    // ---------- APPRECIATION BONUS ----------



    // ---------- BONUS BUDGET ----------



    // ---------- CONTRACT ----------
    void createContract(Contract contract, String email) throws IllegalAccessException;



    // ---------- DAYS OFF REQUEST ----------
    void createDaysOffRequest(DaysOffRequest daysOffRequest, String email) throws IllegalAccessException;
    DaysOffRequest getDaysOffRequestByID(long documentID);
    void updateDaysOffRequest(long documentID, AddDaysOffRequestHelper addDaysOffRequestHelper, String email) throws IllegalAccessException;
    void removeDaysOffRequest(long documentID, String email) throws IllegalAccessException;
    List<DaysOffRequest> getAllDaysOffRequests();


    // ---------- DELEGATION REQUEST ----------
    void createDelegationRequest(DelegationRequest delegationRequest);
    DelegationRequest getDelegationRequestByID(long documentID);
    void updateDelegationRequest(long documentID, AddDelegationRequestHelper delegationRequestHelper, String email) throws IllegalAccessException;
    void removeDelegationRequest(long documentID, String email) throws IllegalAccessException;
    List<DelegationRequest> getAllDelegationRequests();

    // ---------- EMPLOYEE ----------
    List<Employee> getAllEmployees();
    Employee getEmployee(long id, String email) throws IllegalAccessException;
    Employee getEmployeeByEmail(String email, String requestingEmail) throws IllegalAccessException;
    Employee updateEmployee(Employee employee, String email) throws IllegalAccessException;
    void createEmployee(AddEmployeeHelper helper);
    void removeEmployee(long id);
    List<AbsentEmployees> getAbsentEmployees(LocalDate from, LocalDate to, String email) throws IllegalAccessException;

    List<DelegationRequest> getEmployeeDelegationRequests(long id, LocalDate from, LocalDate to, String email) throws IllegalAccessException;
    List<DaysOffRequest> getEmployeeDaysOffRequests(long id, LocalDate from, LocalDate to, String email) throws IllegalAccessException;

    int getEmployeesDaysOffLeft(long id, String email) throws IllegalAccessException;


    // ---------- PERMISSIONS ----------


}
