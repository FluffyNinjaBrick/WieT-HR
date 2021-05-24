package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.enums.UserRole;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.jpaRepos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
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


    // ---------- APPRECIATION BONUS ---------
    @Override
    public List<AppreciationBonus> getEmployeeBonuses(long id) {
        return this.employeeRepository
                .findById(id)
                .orElseThrow()
                .getAppreciationBonusList();
    }

    @Override
    public void addAppreciationBonus(AddAppreciationBonusHelper bonusHelper) {
        Employee employee = employeeRepository.findById(bonusHelper.getEmployeeId()).orElseThrow();
        BonusBudget bonusBudget = bonusBudgetRepository.findById(bonusHelper.getBonusBudgetId()).orElseThrow();
        float checkBudget = bonusBudget.getValue();
        for (AppreciationBonus bonus : this.appreciationBonusRepository.findAll()) {
            if (bonus.getBonusBudget().getId() == bonusBudget.getId()) {
                checkBudget -= bonus.getValue();
            }
        }
        if (checkBudget - bonusHelper.getValue() < 0) {
            throw new IllegalArgumentException("Bonus cannot be added, budget would be negative");
        }
        AppreciationBonus appreciationBonus = new AppreciationBonus(
                employee,
                bonusHelper.getYearMonth(),
                bonusHelper.getDateGenerated(),
                bonusHelper.getValue(),
                bonusBudget
        );
        employee.getAppreciationBonusList().add(appreciationBonus);
        bonusBudget.getBonusList().add(appreciationBonus);

        employeeRepository.save(employee);
        bonusBudgetRepository.save(bonusBudget);
        appreciationBonusRepository.save(appreciationBonus);
    }


    // ---------- BONUS BUDGET ---------
    @Override
    public void createBonusBudget(BonusBudgetHelper helper) {

        BonusBudget currentBudget = null;

        // check if budget for that year already exists, if so - abort, if not - create it
        try {
            currentBudget = this.getBudgetForYear(helper.getYear());
        } catch (Exception e) { /* ignore the not found exception, that's what we want */ }

        if (currentBudget != null)
            throw new IllegalStateException("Error: budget for year {helper.getYear()} already exists");
        this.bonusBudgetRepository.save(new BonusBudget(helper.getYear(), helper.getBudgetSize()));

    }

    @Override
    public void modifyBonusBudget(BonusBudgetHelper helper) throws IllegalStateException {

        BonusBudget budget = this.bonusBudgetRepository.findById(helper.getBudgetId()).orElseThrow();

        // check if we can modify the budget
        try {
            if (!budget.getBonusList().isEmpty() || this.getBudgetForYear(helper.getYear()) != null)
                throw new IllegalStateException("Error: cannot change year for budget");
        } catch (NoSuchElementException e) { /* ignore the not found exception, that's what we want */ }

        budget.setValue(helper.getBudgetSize());
        this.bonusBudgetRepository.save(budget);
    }

    @Override
    public BonusBudget getBudgetForYear(Year year) {
        return this.bonusBudgetRepository.getBonusBudgetByYear(year).orElseThrow();
    }

    @Override
    public float getBonusBudgetLeft(BonusBudget bonusBudget) {
        float result = bonusBudget.getValue();
        for (AppreciationBonus bonus : bonusBudget.getBonusList()) {
            result -= bonus.getValue();
        }
        return result;
    }

    @Override
    public List<Float> getBonusBudgetUsagePerMonth(BonusBudget bonusBudget) {
        List<AppreciationBonus> bonusList = bonusBudget.getBonusList();
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            result.add(0.0f);
        }
        int index;
        float value;
        for (AppreciationBonus bonus : bonusList) {
            index = bonus.getYearMonth().getMonthValue() - 1;
            value = bonus.getValue() + result.get(index);
            result.add(index, value);
        }
        return result;
    }

    @Override
    public BonusesOfAllEmployeesHelper getBonusesForYear(Year year) {
        BonusBudget budget = bonusBudgetRepository.getBonusBudgetByYear(year).orElseThrow();
        BonusesOfAllEmployeesHelper bonusesOfAllEmployeesHelper = new BonusesOfAllEmployeesHelper(budget.getId(), year, budget.getValue());

        for (Employee employee : this.employeeRepository.findAll()) {
            BonusesOfEmployeeHelper bonusesOfEmployeeHelper = new BonusesOfEmployeeHelper(
                    employee.getId(),
                    employee.getFullName()
            );
            for (AppreciationBonus bonus : employee.getAppreciationBonusList()) {
                if (bonus.getYearMonth().getYear() == year.getValue()) {
                    bonusesOfAllEmployeesHelper.useBudget(bonus.getValue());
                    int monthIndex = bonus.getYearMonth().getMonthValue() - 1;
                    float currentEmployeeSum = bonusesOfEmployeeHelper.getEmployeeBonuses().remove(monthIndex);
                    currentEmployeeSum += bonus.getValue();
                    bonusesOfEmployeeHelper.getEmployeeBonuses().add(monthIndex, currentEmployeeSum);
                    bonusesOfEmployeeHelper.addBonusToTotal(bonus.getValue());

                    float currentAllSum = bonusesOfAllEmployeesHelper.getMonthlySummary().remove(monthIndex);
                    currentAllSum += bonus.getValue();
                    bonusesOfAllEmployeesHelper.getMonthlySummary().add(monthIndex, currentAllSum);
                }
            }
            bonusesOfAllEmployeesHelper.getBonuses().add(bonusesOfEmployeeHelper);
        }

        return bonusesOfAllEmployeesHelper;
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

    public List<Contract> getAvailableContracts(String email) {
        List<Contract> contracts = new ArrayList<>();
        List<Contract> allContracts = this.contractRepository.findAll();
        Employee employee = getEmployeeByEmail(email);

        if (employee.getUserRole().equals(UserRole.ADMIN)) {
            contracts = allContracts;
        } else if (employee.getUserRole().equals(UserRole.MANAGER)) {
            for (Contract contract : allContracts) {
                if (contract.getEmployee() == employee.getId() || employee.getPermissions().getManagedUsers().contains(contract.getEmployee())) {
                    contracts.add(contract);
                }
            }
        } else {
            for (Contract contract : allContracts) {
                if (contract.getEmployee() == employee.getId()) {
                    contracts.add(contract);
                }
            }
        }

        return contracts;
    }

    @Override
    public void createAnnex(AddAnnexHelper addAnnexHelper) {
        Contract contract = this.contractRepository.findById(addAnnexHelper.getContractId()).orElseThrow();
        Employee employee = this.employeeRepository.findById(addAnnexHelper.getEmployeeId()).orElseThrow();
        Contract annex = new Contract(
                employee,
                addAnnexHelper.getDateFrom(),
                addAnnexHelper.getDateTo(),
                addAnnexHelper.getSalary(),
                addAnnexHelper.getDutyAllowance(),
                addAnnexHelper.getWorkingHours(),
                addAnnexHelper.getAnnualLeaveDays(),
                addAnnexHelper.getType(),
                new ArrayList<>()
        );
        annex = contractRepository.save(annex);
        contract.addAnnex(annex);
        contractRepository.save(contract);
    }

    @Override
    public void deleteAnnex(long annexId) {
        Contract annexToBeRemoved = this.contractRepository.findById(annexId).orElseThrow();
        if (annexToBeRemoved.isSigned()) {
            throw new IllegalArgumentException("Cannot delete signed annex");
        }
        Contract contract = null;

        for (Contract c : this.contractRepository.findAll()) {
            if (c.getAnnexes().contains(annexToBeRemoved)) {
                contract = c;
            }
        }

        if (contract == null) {
            throw new IllegalArgumentException("Annex id doesnt match");
        }

        contract.getAnnexes().remove(annexToBeRemoved);
        this.contractRepository.save(contract);
        this.contractRepository.delete(annexToBeRemoved);
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

    @Override
    public void signDaysOffRequest(long documentId, String email) {
        Employee signer = this.getEmployeeByEmail(email);
        DaysOffRequest request = this.getDaysOffRequestByID(documentId);

        long duration =  ChronoUnit.DAYS.between(request.getDateFrom(), request.getDateTo());

        Employee employee = request.employeeObject();
        employee.reduceDaysOffRequestLeft((int) (duration + 1));

        request.sign(signer);

        this.daysOffRequestRepository.save(request);
        this.employeeRepository.save(employee);
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

    @Override
    public void signDelegationRequest(long documentId, String email) {
        Employee signer = this.getEmployeeByEmail(email);
        DelegationRequest request = this.getDelegationRequestByID(documentId);

        request.sign(signer);
        this.delegationRequestRepository.save(request);
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
    public void updatePassword(UpdatePasswordHelper helper) {
        Employee employee = this.getEmployee(helper.getId());
        employee.setPassword(helper.getNewPassword());
        this.employeeRepository.save(employee);
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
    public EmployeesSalariesHelper getSalaries(int year, String email) {
        Map<Long, EmployeeSalaryHelper> employeeSalarySumMap = new HashMap<>();
        float[] monthlySum = new float[12];
        Arrays.fill(monthlySum, 0);
        List<Contract> contracts = getAvailableContracts(email);
        int[] monthLength = new int[12];
        for (int i = 0; i < 12; i++) {
            monthLength[i] = YearMonth.of(year, i + 1).lengthOfMonth();
        }

        for (Contract contract : contracts) {
            if (contract.getAnnexes().isEmpty()) { // most recent?
                for (int i = 0; i < 12; i++) {
                    if (contract.getDateFrom().isBefore(ChronoLocalDate.from(LocalDate.of(year, i + 1, 2))) &&
                            (contract.getDateTo() == null ||
                                    contract.getDateTo().isAfter(ChronoLocalDate.from(LocalDate.of(year, i + 1, monthLength[i] - 1))))) {
                        monthlySum[i] += contract.getSalary();
                        if (employeeSalarySumMap.containsKey(contract.getEmployee())) {
                            EmployeeSalaryHelper salaryHelper = employeeSalarySumMap.get(contract.getEmployee());
                            salaryHelper.getMonthlySum()[i] += contract.getSalary();
                            salaryHelper.increaseSum(contract.getSalary());
                        } else {
                            Employee employee = this.employeeRepository.findById(contract.getEmployee()).orElseThrow();
                            float[] monthly = new float[12];
                            Arrays.fill(monthly, 0);
                            monthly[i] = contract.getSalary();
                            EmployeeSalaryHelper salaryHelper = new EmployeeSalaryHelper(
                                    employee.getId(),
                                    employee.getFullName(),
                                    monthly,
                                    contract.getSalary()
                            );
                            employeeSalarySumMap.put(contract.getEmployee(), salaryHelper);
                        }
                    }
                }
            }
        }

        return new EmployeesSalariesHelper(monthlySum, employeeSalarySumMap);
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
