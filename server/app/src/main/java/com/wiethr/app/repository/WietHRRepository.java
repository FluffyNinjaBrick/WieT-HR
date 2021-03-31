package com.wiethr.app.repository;

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
    private final EmployeeRepository employeeRepository;
    private final PermissionsRepository permissionsRepository;
    private final DaysOffRequestRepository daysOffRequestRepository;

    @Autowired
    public WietHRRepository(
            AppreciationBonusRepository appreciationBonusRepository,
            BonusBudgetRepository bonusBudgetRepository,
            ContractRepository contractRepository,
            DaysOffRequestRepository daysOffRequestRepository,
            EmployeeRepository employeeRepository,
            PermissionsRepository permissionsRepository) {
        this.appreciationBonusRepository = appreciationBonusRepository;
        this.bonusBudgetRepository = bonusBudgetRepository;
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
        this.permissionsRepository = permissionsRepository;
        this.daysOffRequestRepository = daysOffRequestRepository;
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
