package com.wiethr.app.repository;

import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.AbsentEmployees;
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


    // ---------- DELEGATION REQUEST ----------
    @Override
    public void createDelegationRequest(DelegationRequest delegationRequest) {
        this.delegationRequestRepository.save(delegationRequest);
    }

    // ---------- EMPLOYEE ----------
    @Override
    public List<Employee> getAllEmployees() {
        return this.employeeRepository.findAll();
    }

    public Optional<Employee> getEmployee(long id) {
        return this.employeeRepository.findById(id);
    }

    public Employee updateEmployee(Employee employee) {
        this.permissionsRepository.save(employee.getPermissions());
        return this.employeeRepository.save(employee);
    }

    @Override
    public void createEmployee(Employee newEmployee) {
        this.permissionsRepository.save(newEmployee.getPermissions());
        this.employeeRepository.save(newEmployee);
    }

    @Override
    public void removeEmployee(long id) {
        Optional<Employee> optional = getEmployee(id);
        if (optional.isEmpty()) return;                       // check if employee even exists
        if (getSignedContractsForEmployee(id).size() != 0)    // check if employee has signed contracts
            throw new IllegalStateException("Error: employee has signed contracts, cannot remove");

        this.employeeRepository.delete(optional.get());
    }

}
