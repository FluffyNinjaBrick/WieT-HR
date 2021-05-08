package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.enums.UserRole;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.jpaRepos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDate;


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
    public void createContract(AddContractHelper helper) {

        Contract contract = new Contract();
        Employee employee = this.getEmployee(helper.getEmployeeID());

        // inherited from document
        contract.setEmployee(employee);
        contract.setNameAtSigning(employee.getFullName());
        contract.setDateIssued(LocalDate.now());
        contract.setSigned(false);
        contract.setDateFrom(helper.getDateFrom());
        contract.setDateTo(helper.getDateTo());

        // own fields
        contract.setSalary(helper.getSalary());
        contract.setDutyAllowance(helper.getDutyAllowance());
        contract.setWorkingHours(helper.getWorkingHours());
        contract.setAnnualLeaveDays(helper.getAnnualLeaveDays());
        contract.setType(helper.getType());
        contract.setAnnexes(new ArrayList<>());

        this.contractRepository.save(contract);
    }

    private List<Contract> getSignedContractsForEmployee(long id) {
        ArrayList<Contract> contracts = new ArrayList<>();

        for (Contract c: this.contractRepository.findAll())
            if (c.getEmployee() == id && c.isSigned())
                contracts.add(c);

        return contracts;
    }


    // ---------- DAYS OFF REQUEST ----------
    @Override
    public void createDaysOffRequest(DaysOffRequestHelper helper, String email) {

        DaysOffRequest request = new DaysOffRequest();
        Employee employee = this.getEmployeeByEmail(email);

        // inherited from document
        request.setEmployee(employee);
        request.setNameAtSigning(employee.getFullName());
        request.setDateIssued(LocalDate.now());
        request.setSigned(false);
        request.setDateFrom(helper.getDateFrom());
        request.setDateTo(helper.getDateTo());

        // own
        request.setLeaveType(helper.getLeaveType());

        this.daysOffRequestRepository.save(request);
    }

    @Override
    public DaysOffRequest getDaysOffRequestByID(long documentID) {
        return this.daysOffRequestRepository.findById(documentID).orElseThrow();
    }

    @Override
    public void updateDaysOffRequest(DaysOffRequestHelper helper, Employee requestOwner) {
        DaysOffRequest daysOffRequest = this.daysOffRequestRepository.findById(helper.getDocumentId()).orElseThrow();

        // daysOffRequest
        daysOffRequest.setLeaveType(helper.getLeaveType());
        // document
        daysOffRequest.setDateFrom(helper.getDateFrom());
        daysOffRequest.setDateTo(helper.getDateTo());
        daysOffRequest.setDateIssued(LocalDate.now());
        daysOffRequest.setSigned(false);
        daysOffRequest.setEmployee(requestOwner);
        daysOffRequest.setNameAtSigning(requestOwner.getFullName());

        this.daysOffRequestRepository.save(daysOffRequest);
    }

    @Override
    public void removeDaysOffRequest(long documentID) {
        DaysOffRequest daysOffRequest = this.daysOffRequestRepository.findById(documentID).orElseThrow();

        if (daysOffRequest.isSigned()) {
            throw new IllegalArgumentException("ERROR: Cannot remove signed DaysOffRequest document");
        } else {
            this.daysOffRequestRepository.delete(daysOffRequest);
        }
    }

    @Override
    public List<DaysOffRequest> getAllDaysOffRequests() {
        return this.daysOffRequestRepository.findAll();
    }


    // ---------- DELEGATION REQUEST ----------
    @Override
    public void createDelegationRequest(DelegationRequestHelper helper, String email) {
        DelegationRequest request = new DelegationRequest();

        Employee employee = this.getEmployeeByEmail(email);

        // inherited from document
        request.setEmployee(employee);
        request.setNameAtSigning(employee.getFullName());
        request.setDateIssued(LocalDate.now());
        request.setSigned(false);
        request.setDateFrom(helper.getDateFrom());
        request.setDateTo(helper.getDateTo());

        // own
        request.setDestination(helper.getDestination());
        this.delegationRequestRepository.save(request);
    }

    @Override
    public DelegationRequest getDelegationRequestByID(long documentID) {
        return this.delegationRequestRepository.findById(documentID).orElseThrow();
    }

    @Override
    public void updateDelegationRequest(DelegationRequestHelper helper, Employee requestOwner) {

        DelegationRequest delegationRequest = this.delegationRequestRepository.findById(helper.getDocumentId()).orElseThrow();

        // delegationRequest
        delegationRequest.setDestination(helper.getDestination());
        // document
        delegationRequest.setDateFrom(helper.getDateFrom());
        delegationRequest.setDateTo(helper.getDateTo());
        delegationRequest.setDateIssued(LocalDate.now());
        delegationRequest.setSigned(false);
        delegationRequest.setEmployee(requestOwner);
        delegationRequest.setNameAtSigning(requestOwner.getFullName());

        this.delegationRequestRepository.save(delegationRequest);
    }

    @Override
    public void removeDelegationRequest(long documentID) {
        DelegationRequest delegationRequest = this.delegationRequestRepository.findById(documentID).orElseThrow();

        if(delegationRequest.isSigned()){
            throw new IllegalArgumentException("ERROR: Cannot remove signed DaysOffRequest document");
        } else{
            this.delegationRequestRepository.delete(delegationRequest);
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
    public Employee getEmployee(long id) {
        return this.employeeRepository.findById(id).orElseThrow();
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return this.employeeRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public void updateEmployee(UpdateEmployeeHelper helper) {
//        this.permissionsRepository.save(changedEmployee.getPermissions());

        Employee employee = this.getEmployee(helper.getId());
        employee.setFirstName(helper.getFirstName());
        employee.setLastName(helper.getLastName());
        employee.setPhone(helper.getPhone());
        employee.setAddress(helper.getAddress());
        employee.setUserRole(helper.getRole());

        this.employeeRepository.save(employee);
    }

    @Override
    public void updateEmployeePermissions(PermissionHelper helper) {
        Permissions permissions = this.getEmployee(helper.getEmployeeId()).getPermissions();
        permissions.update(helper, this);
        this.permissionsRepository.save(permissions);
    }

    @Override
    public void createEmployee(AddEmployeeHelper helper) {

        // create and save permissions
        PermissionHelper permHelper = helper.getPermissionHelper();
        Permissions permissions = new Permissions(permHelper.isAddUsers(), permHelper.isModifyBonusBudget());
        for (Employee user: this.employeeRepository.findAllById(permHelper.getManagedUsers()))
            permissions.addManagedUser(user);

//        this.permissionsRepository.save(permissions);

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

    @Override
    public List<DelegationRequest> getEmployeeDelegationRequests(long id, LocalDate from, LocalDate to) {

        if(to.isBefore(from)){
            throw new IllegalArgumentException("Error: getEmployeeDelegationRequests function wrong dates: from, to");
        }

        ArrayList<DelegationRequest> requests = new ArrayList<>(this.delegationRequestRepository.findAll());
        return (List<DelegationRequest>) Utilities.getDocumentsBetweenDates(requests, id, from, to);
    }

    @Override
    public List<DaysOffRequest> getEmployeeDaysOffRequests(long id, LocalDate from, LocalDate to) {

        if(to.isBefore(from)){
            throw new IllegalArgumentException("Error: getEmployeeDaysOffRequests function wrong dates: from, to");
        }

        ArrayList<DaysOffRequest> requests = new ArrayList<>(this.daysOffRequestRepository.findAll());
        return (List<DaysOffRequest>) Utilities.getDocumentsBetweenDates(requests, id, from, to);
    }

    @Override
    public EmployeeDaysOffDetails getEmployeeDaysOffLeft(long id) {

        // get all requests from this year
        List<DaysOffRequest> daysOffRequestList = daysOffRequestRepository
                .findAllByEmployeeIdAndDateFromAfter(id, LocalDate.of(LocalDate.now().getYear(), 1, 1));

        Employee employee = employeeRepository.findById(id).orElseThrow();
        int daysLeft = employee.getLastYearDaysOff() + employee.getThisYearDaysOff();
        int daysUsed = 0;

        for (DaysOffRequest request : daysOffRequestList) {
            if (!request.isSigned() || request.getDateTo() == null) continue;   // ignore all requests that aren't signed or are indefinite
            daysUsed = Utilities.getTotalWorkingDays(request.getDateFrom(), request.getDateTo());
        }

        return new EmployeeDaysOffDetails(id, employee.getFullName(), daysUsed, daysLeft);
    }

    @Override
    public GroupDaysOffDetails getGroupDaysOffLeft(String email) {

        List<EmployeeDaysOffDetails> employeeDaysOffDetails = new ArrayList<>();
        int totalDaysOffUsed = 0;
        int totalDaysOffLeft = 0;

        // iterate over all subordinates of person asking
        Employee asking = this.getEmployeeByEmail(email);
        List<Employee> subordinates;
        if (asking.getUserRole().equals(UserRole.ADMIN)) subordinates = this.employeeRepository.findAll();
        else subordinates = asking.getPermissions().managedUsersObject();

        for (Employee e: subordinates) {
            EmployeeDaysOffDetails details = this.getEmployeeDaysOffLeft(e.getId());
            totalDaysOffUsed += details.getDaysOffUsed();
            totalDaysOffLeft += details.getDaysOffLeft();
            employeeDaysOffDetails.add(details);
        }

        return new GroupDaysOffDetails(employeeDaysOffDetails, totalDaysOffUsed, totalDaysOffLeft);

    }

    @Override
    public List<AppreciationBonus> getEmployeeBonuses(long id) {
        return this.employeeRepository
                .findById(id)
                .orElseThrow()
                .getAppreciationBonusList();
    }

    @Override
    public Permissions createPermissionsFromHelper(PermissionHelper helper) {
        Permissions permissions = new Permissions(helper.isAddUsers(), helper.isModifyBonusBudget());
        for (Employee employee : this.employeeRepository.findAllById(helper.getManagedUsers())) {
            permissions.addManagedUser(employee);
        }
        return permissions;
    }
}
