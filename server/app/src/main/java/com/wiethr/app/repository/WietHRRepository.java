package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.jpaRepos.*;
import com.wiethr.app.security.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Repository
public class WietHRRepository implements IWietHRRepository {

    private final AppreciationBonusRepository appreciationBonusRepository;
    private final BonusBudgetRepository bonusBudgetRepository;
    private final ContractRepository contractRepository;
    private final DelegationRequestRepository delegationRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final PermissionsRepository permissionsRepository;
    private final DaysOffRequestRepository daysOffRequestRepository;
    private final RoleValidator roleValidator;

    @Autowired
    public WietHRRepository(
            AppreciationBonusRepository appreciationBonusRepository,
            BonusBudgetRepository bonusBudgetRepository,
            ContractRepository contractRepository,
            DelegationRequestRepository delegationRequestRepository, DaysOffRequestRepository daysOffRequestRepository,
            EmployeeRepository employeeRepository,
            PermissionsRepository permissionsRepository,
            RoleValidator roleValidator) {
        this.appreciationBonusRepository = appreciationBonusRepository;
        this.bonusBudgetRepository = bonusBudgetRepository;
        this.contractRepository = contractRepository;
        this.delegationRequestRepository = delegationRequestRepository;
        this.employeeRepository = employeeRepository;
        this.permissionsRepository = permissionsRepository;
        this.daysOffRequestRepository = daysOffRequestRepository;
        this.roleValidator = roleValidator;
    }

    // ---------- CONTRACT ----------
    @Override
    public void createContract(Contract contract, String email) throws IllegalAccessException {
        this.roleValidator.validate(this.getEmployeeByEmail(email), contract.getEmployee().getId());
        this.contractRepository.save(contract);
    }

    private List<Contract> getSignedContractsForEmployee(long id) {
        ArrayList<Contract> contracts = new ArrayList<>();

        for (Contract c: this.contractRepository.findAll())
            if (c.getEmployee().getId() == id && c.isSigned())
                contracts.add(c);

        return contracts;
    }


    // ---------- DAYS OFF REQUEST ----------
    @Override
    public void createDaysOffRequest(DaysOffRequest daysOffRequest, String email) throws IllegalAccessException {
        this.roleValidator.validate(this.getEmployeeByEmail(email), daysOffRequest.getEmployee().getId());
        this.daysOffRequestRepository.save(daysOffRequest);
    }

    @Override
    public DaysOffRequest getDaysOffRequestByID(long documentID) {
        return this.daysOffRequestRepository.findById(documentID).orElseThrow();
    }

    @Override
    public void updateDaysOffRequest(
            long documentID,
            AddDaysOffRequestHelper addDaysOffRequestHelper,
            String email
    ) throws IllegalAccessException {
        Optional<DaysOffRequest> daysOffRequest = this.daysOffRequestRepository.findById(documentID);
        Employee employee = this.getEmployee(addDaysOffRequestHelper.getEmployeeID());

        this.roleValidator.validate(this.getEmployeeByEmail(email), daysOffRequest.get().getEmployee().getId());

        DaysOffRequest currentDaysOffRequest = daysOffRequest.get();
        // daysOffRequest
        currentDaysOffRequest.setLeaveType(addDaysOffRequestHelper.getLeaveType());
        // document
        currentDaysOffRequest.setDateFrom(addDaysOffRequestHelper.getDateFrom());
        currentDaysOffRequest.setDateTo(addDaysOffRequestHelper.getDateTo());
        currentDaysOffRequest.setDateIssued(LocalDate.now());
        currentDaysOffRequest.setSigned(false);
        currentDaysOffRequest.setEmployee(employee);
        currentDaysOffRequest.setNameAtSigning(employee.getFirstName() + " " + employee.getLastName());

        this.daysOffRequestRepository.save(currentDaysOffRequest);
    }

    @Override
    public void removeDaysOffRequest(long documentID, String email) throws IllegalAccessException {
        Optional<DaysOffRequest> daysOffRequest = this.daysOffRequestRepository.findById(documentID);
        if(daysOffRequest.isEmpty()) return;

        this.roleValidator.validate(this.getEmployeeByEmail(email), daysOffRequest.get().getEmployee().getId());

        DaysOffRequest currentDaysOffRequest = daysOffRequest.get();
        if (currentDaysOffRequest.isSigned()) {
            throw new IllegalArgumentException("ERROR: Cannot remove signed DaysOffRequest document");
        } else {
            this.daysOffRequestRepository.delete(currentDaysOffRequest);
        }
    }

    @Override
    public List<DaysOffRequest> getAllDaysOffRequests() {
        return this.daysOffRequestRepository.findAll();
    }


    // ---------- DELEGATION REQUEST ----------
    @Override
    public void createDelegationRequest(DelegationRequest delegationRequest) {
        this.delegationRequestRepository.save(delegationRequest);
    }

    @Override
    public DelegationRequest getDelegationRequestByID(long documentID) {
        return this.delegationRequestRepository.findById(documentID).orElseThrow();
    }

    @Override
    public void updateDelegationRequest(
            long documentID,
            AddDelegationRequestHelper delegationRequestHelper,
            String email
    ) throws IllegalAccessException {
        Optional<DelegationRequest> delegationRequest = this.delegationRequestRepository.findById(documentID);
        Optional<Employee> employee = this.employeeRepository.findById(delegationRequestHelper.getEmployeeID());

        this.roleValidator.validate(this.getEmployeeByEmail(email), delegationRequest.get().getEmployee().getId());

        if(delegationRequest.isPresent() && employee.isPresent()) {
            Employee employeeToSet = employee.get();
            DelegationRequest currentDelegationRequest = delegationRequest.get();
            // delegationRequest
            currentDelegationRequest.setDestination(delegationRequestHelper.getDestination());
            // document
            currentDelegationRequest.setDateFrom(delegationRequestHelper.getDateFrom());
            currentDelegationRequest.setDateTo(delegationRequestHelper.getDateTo());
            currentDelegationRequest.setDateIssued(LocalDate.now());
            currentDelegationRequest.setSigned(false);
            currentDelegationRequest.setEmployee(employeeToSet);
            currentDelegationRequest.setNameAtSigning(employeeToSet.getFirstName() + " " + employeeToSet.getLastName());

            this.delegationRequestRepository.save(currentDelegationRequest);
        }
    }

    @Override
    public void removeDelegationRequest(long documentID, String email) throws IllegalAccessException {
        Optional<DelegationRequest> delegationRequest = this.delegationRequestRepository.findById(documentID);
        if(delegationRequest.isEmpty()) return;

        this.roleValidator.validate(this.getEmployeeByEmail(email), delegationRequest.get().getEmployee().getId());

        DelegationRequest currentDelegationRequest = delegationRequest.get();
        if(currentDelegationRequest.isSigned()){
            throw new IllegalArgumentException("ERROR: Cannot remove signed DaysOffRequest document");
        } else{
            this.delegationRequestRepository.delete(currentDelegationRequest);
        }
    }

    @Override
    public List<DelegationRequest> getAllDelegationRequests() {
        return this.delegationRequestRepository.findAll();
    }


    // ---------- EMPLOYEE ----------
    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(long id, String email) throws IllegalAccessException {
        this.roleValidator.validate(this.getEmployeeByEmail(email), id);
        return this.employeeRepository.findById(id).orElseThrow();
    }

    public Employee getEmployee(long id) {
        return this.employeeRepository.findById(id).orElseThrow();
    }

    @Override
    public Employee getEmployeeByEmail(String email, String requestingEmail) throws IllegalAccessException {
        Employee queried = this.employeeRepository.findByEmail(email).orElseThrow();
        Employee requesting = this.employeeRepository.findByEmail(requestingEmail).orElseThrow();
        this.roleValidator.validate(requesting, queried.getId());
        return queried;
    }

    public Employee getEmployeeByEmail(String email) {
        return this.employeeRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public Employee updateEmployee(Employee changedEmployee, String email) throws IllegalAccessException {
        this.roleValidator.validate(this.getEmployeeByEmail(email), changedEmployee.getId());
        this.permissionsRepository.save(changedEmployee.getPermissions());
        return this.employeeRepository.save(changedEmployee);
    }

    @Override
    public void createEmployee(AddEmployeeHelper helper) {

        // create and save permissions
        PermissionHelper permHelper = helper.getPermissionHelper();
        Permissions permissions = new Permissions(permHelper.isAddUsers(), permHelper.isModifyBonusBudget());
        for (Employee user: this.employeeRepository.findAllById(permHelper.getManagedUsers()))
            permissions.addManagedUser(user);

        this.permissionsRepository.save(permissions);

        // create and save employee
        Employee employee = new Employee();
        employee.setPermissions(permissions);
        employee.setEmail(helper.getEmail());
        employee.setFirstName(helper.getFirstName());
        employee.setLastName(helper.getLastName());
        employee.setPassword(helper.getPassword());
        employee.setAddress(helper.getAddress());
        employee.setPhone(helper.getPhone());
        employee.setUserRole(helper.getUserRole());
        employee.setStatus(helper.getStatus());
        employee.setYearsOfService(helper.getYearsOfService());
        employee.setThisYearDaysOff(helper.getThisYearDaysOff());
        employee.setLastYearDaysOff(helper.getLastYearDaysOff());
        employee.setAppreciationBonusList(new ArrayList<>());

        this.employeeRepository.save(employee);
    }

    @Override
    public void removeEmployee(long id) {
        if (getSignedContractsForEmployee(id).size() != 0)    // check if employee has signed contracts
            throw new IllegalStateException("Error: employee has signed contracts, cannot remove");
        this.employeeRepository.delete(this.getEmployee(id));
    }

    @Override
    public List<AbsentEmployees> getAbsentEmployees(
            LocalDate from,
            LocalDate to,
            String email
    ) throws IllegalAccessException {

        this.roleValidator.validateAbsent(this.getEmployeeByEmail(email));

        ArrayList<AbsentEmployees> absences = new ArrayList<>();

        ArrayList<Document> requests = new ArrayList<>();
        requests.addAll(this.daysOffRequestRepository.findAll());
        requests.addAll(this.delegationRequestRepository.findAll());


        LocalDate current = from;
        do {

            AbsentEmployees absencesForDay = new AbsentEmployees(current);

            for (Document request: requests)
                if (!current.isBefore(request.getDateFrom()) && !current.isAfter(request.getDateTo()))
                    absencesForDay.addEmployee(request.getNameAtSigning());

            absences.add(absencesForDay);
            current = current.plusDays(1);

        } while(!current.isAfter(to));

        return absences;
    }

    @Override
    public List<DelegationRequest> getEmployeeDelegationRequests(
            long id,
            LocalDate from,
            LocalDate to,
            String email
    ) throws IllegalAccessException{

        this.roleValidator.validate(this.getEmployeeByEmail(email), id);

        if(to.isBefore(from)){
            throw new IllegalArgumentException("Error: getEmployeeDelegationRequests function wrong dates: from, to");
        }

        ArrayList<DelegationRequest> requests = new ArrayList<>(this.delegationRequestRepository.findAll());
        return requests.stream().filter(
                req -> req.getEmployee().getId() == id && !req.getDateTo().isBefore(from) && !req.getDateFrom().isAfter(to))
                .sorted(Comparator.comparing(DelegationRequest::getDateFrom).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<DaysOffRequest> getEmployeeDaysOffRequests(
            long id,
            LocalDate from,
            LocalDate to,
            String email
    ) throws IllegalAccessException{

        this.roleValidator.validate(this.getEmployeeByEmail(email), id);

        if(to.isBefore(from)){
            throw new IllegalArgumentException("Error: getEmployeeDaysOffRequests function wrong dates: from, to");
        }

        ArrayList<DaysOffRequest> requests = new ArrayList<>(this.daysOffRequestRepository.findAll());
        return requests.stream().filter(
                req -> req.getEmployee().getId() == id && !req.getDateTo().isBefore(from) && !req.getDateFrom().isAfter(to))
                .sorted(Comparator.comparing(DaysOffRequest::getDateFrom).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public int getEmployeesDaysOffLeft(long id, String email) throws IllegalAccessException {

        this.roleValidator.validate(this.getEmployeeByEmail(email), id);

        List<DaysOffRequest> daysOffRequestList = daysOffRequestRepository.findAllByEmployeeIdAndDateFromAfter(id, LocalDate.now());
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            int daysLeft = employee.getLastYearDaysOff() + employee.getThisYearDaysOff();
            for (DaysOffRequest request : daysOffRequestList) {
                Date from = new Date(request.getDateFrom().getYear(), request.getDateFrom().getMonthValue(), request.getDateFrom().getDayOfMonth());
                Date to = new Date(request.getDateTo().getYear(), request.getDateTo().getMonthValue(), request.getDateTo().getDayOfMonth());
                int days = (int) (to.getTime() - from.getTime()) / 1000 / 60 / 60 / 24;
                daysLeft -= days;
            }
            return daysLeft;
        }
        // this should not happen
        return -1;
    }
}
