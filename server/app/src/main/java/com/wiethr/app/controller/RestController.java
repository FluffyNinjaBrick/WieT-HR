package com.wiethr.app.controller;


import com.wiethr.app.model.Employee;
import com.wiethr.app.model.Permissions;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final WietHRRepository repository;

    @Autowired
    public RestController(WietHRRepository repository) {
        this.repository = repository;
    }


    // ---------- EMPLOYEE ----------
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }

    @PostMapping("/employee/{employeeId}/edit/data")
    @ResponseBody
    public Employee updateEmployeeData(@PathVariable long employeeId, @RequestBody Employee updatedEmployee) {
        Optional<Employee> currentEmployeeOptional = repository.getEmployee(employeeId);
        if (currentEmployeeOptional.isPresent()) {
            Employee currentEmployee = currentEmployeeOptional.get();
            if (currentEmployee.getId() == employeeId) {
                currentEmployee.setFirstName(updatedEmployee.getFirstName());
                currentEmployee.setLastName(updatedEmployee.getLastName());
                currentEmployee.setEmail(updatedEmployee.getEmail());
                currentEmployee.setPhone(updatedEmployee.getPhone());
                currentEmployee.setAddress(updatedEmployee.getAddress());
                return repository.updateOrAddEmployee(currentEmployee);
            }
        }
        return null;
    }

    @PostMapping("/employee/{employeeId}/edit/permissions")
    @ResponseBody
    public Employee updateEmployeePermissions(@PathVariable long employeeId, @RequestBody Permissions updatedPermissions) {
        Optional<Employee> employeeOptional = repository.getEmployee(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            if (employee.getId() == employeeId) {
                employee.getPermissions().setManagedUsers(updatedPermissions.getManagedUsers());
                employee.getPermissions().setModifyBonusBudget(updatedPermissions.isModifyBonusBudget());
                employee.setPermissions(updatedPermissions);
                return repository.updateOrAddEmployee(employee);
            }
        }
        return null;
    }

    @PostMapping("/employees/{employeeId}/edit/subordinates")
    @ResponseBody
    public Employee updateSubordinatesOfEmployee(@PathVariable long employeeId, @RequestBody long[] subordinates) {
        Optional<Employee> employeeOptional = repository.getEmployee(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            List<Employee> managedUsers = new ArrayList<>();
            for (long l : subordinates) {
                Optional<Employee> employeeOptionalTmp = repository.getEmployee(l);
                employeeOptionalTmp.ifPresent(managedUsers::add);
            }
            employee.getPermissions().setManagedUsers(managedUsers);
            return repository.updateOrAddEmployee(employee);
        }
        return null;
    }

}
