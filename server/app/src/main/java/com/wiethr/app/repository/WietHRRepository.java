package com.wiethr.app.repository;

import com.wiethr.app.model.Contract;
import com.wiethr.app.model.DaysOffRequest;
import com.wiethr.app.model.DelegationRequest;
import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.jpaRepos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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


    // ---------- DAYS OFF REQUEST ----------
    @Override
    public void createDaysOffRequest(DaysOffRequest daysOffRequest) {
        this.daysOffRequestRepository.save(daysOffRequest);
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

    public Employee updateOrAddEmployee(Employee employee) {
        this.permissionsRepository.save(employee.getPermissions());
        return this.employeeRepository.save(employee);
    }



}
