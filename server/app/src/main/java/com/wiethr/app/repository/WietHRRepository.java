package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.jpaRepos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class WietHRRepository implements IWietHRRepository {

    private final AppreciationBonusRepository appreciationBonusRepository;
    private final BonusBudgetRepository bonusBudgetRepository;
    private final ContractRepository contractRepository;
    private final DelegationRequestRepository delegationRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final PermissionsRepository permissionsRepository;
    private final DaysOffRequestRepository daysOffRequestRepository;

    @Autowired
    public WietHRRepository(
            AppreciationBonusRepository appreciationBonusRepository,
            BonusBudgetRepository bonusBudgetRepository,
            ContractRepository contractRepository,
            DelegationRequestRepository delegationRequestRepository, DaysOffRequestRepository daysOffRequestRepository,
            EmployeeRepository employeeRepository,
            PermissionsRepository permissionsRepository) {
        this.appreciationBonusRepository = appreciationBonusRepository;
        this.bonusBudgetRepository = bonusBudgetRepository;
        this.contractRepository = contractRepository;
        this.delegationRequestRepository = delegationRequestRepository;
        this.employeeRepository = employeeRepository;
        this.permissionsRepository = permissionsRepository;
        this.daysOffRequestRepository = daysOffRequestRepository;
    }


    // ---------- CONTRACT ----------
    @Override
    public void createContract(Contract contract) {
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
    public void createDaysOffRequest(DaysOffRequest daysOffRequest) {
        this.daysOffRequestRepository.save(daysOffRequest);
    }

    @Override
    public void updateDaysOffRequest(long documentID, AddDaysOffRequestHelper addDaysOffRequestHelper) {
        Optional<DaysOffRequest> daysOffRequest = this.daysOffRequestRepository.findById(documentID);
        Optional<Employee> employee = this.employeeRepository.findById(addDaysOffRequestHelper.getEmployeeID());

        if(daysOffRequest.isPresent() && employee.isPresent()){
            Employee employeeToSet = employee.get();
            DaysOffRequest currentDaysOffRequest = daysOffRequest.get();
            // daysOffRequest
            currentDaysOffRequest.setLeaveType(addDaysOffRequestHelper.getLeaveType());
            // document
            currentDaysOffRequest.setDateFrom(addDaysOffRequestHelper.getDateFrom());
            currentDaysOffRequest.setDateTo(addDaysOffRequestHelper.getDateTo());
            currentDaysOffRequest.setDateIssued(LocalDate.now());
            currentDaysOffRequest.setSigned(false);
            currentDaysOffRequest.setEmployee(employeeToSet);
            currentDaysOffRequest.setNameAtSigning(employeeToSet.getFirstName()+" "+employeeToSet.getLastName());

            this.daysOffRequestRepository.save(currentDaysOffRequest);
        }
    }

    @Override
    public void removeDaysOffRequest(long documentID) {
        Optional<DaysOffRequest> daysOffRequest = this.daysOffRequestRepository.findById(documentID);
        if(daysOffRequest.isEmpty()) return;

        DaysOffRequest currentDaysOffRequest = daysOffRequest.get();
        if(currentDaysOffRequest.isSigned()){
            throw new IllegalArgumentException("ERROR: Cannot remove signed DaysOffRequest document");
        } else{
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
    public void updateDelegationRequest(long documentID, AddDelegationRequestHelper delegationRequestHelper) {
        Optional<DelegationRequest> delegationRequest = this.delegationRequestRepository.findById(documentID);
        Optional<Employee> employee = this.employeeRepository.findById(delegationRequestHelper.getEmployeeID());

        if(delegationRequest.isPresent() && employee.isPresent()){
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
            currentDelegationRequest.setNameAtSigning(employeeToSet.getFirstName()+" "+employeeToSet.getLastName());

            this.delegationRequestRepository.save(currentDelegationRequest);
        }
    }

    @Override
    public void removeDelegationRequest(long documentID) {
        Optional<DelegationRequest> delegationRequest = this.delegationRequestRepository.findById(documentID);
        if(delegationRequest.isEmpty()) return;

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

    public Optional<Employee> getEmployee(long id) {
        return this.employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmail(String email) {
        return this.employeeRepository.findByEmail(email);
    }

    public Employee updateEmployee(Employee employee) {
        this.permissionsRepository.save(employee.getPermissions());
        return this.employeeRepository.save(employee);
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
        Optional<Employee> optional = getEmployee(id);
        if (optional.isEmpty()) return;                       // check if employee even exists
        if (getSignedContractsForEmployee(id).size() != 0)    // check if employee has signed contracts
            throw new IllegalStateException("Error: employee has signed contracts, cannot remove");

        this.employeeRepository.delete(optional.get());
    }

    @Override
    public List<AbsentEmployees> getAbsentEmployees(LocalDate from, LocalDate to) {

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
}
