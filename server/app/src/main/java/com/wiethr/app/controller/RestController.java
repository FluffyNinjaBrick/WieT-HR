package com.wiethr.app.controller;


import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.WietHRRepository;
import com.wiethr.app.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestController {

    private final WietHRRepository repository;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    @Autowired
    public RestController(WietHRRepository repository, AuthenticationManager authenticationManager, MyUserDetailService userDetailService, JwtUtil jwtUtil) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }


    // ---------- AUTHENTICATION ------------------
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e){
//            throw new Exception("Incorrect email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
        final MyUserDetails userDetails = (MyUserDetails) this.userDetailService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails.getUserRole(), userDetails.getId()));
    }

    // ---------- CONTRACT ----------
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @PostMapping(value = "/documents/create/contract")
    public void createContract(@RequestBody AddContractHelper helper, @RequestHeader("Authorization") String token) throws IllegalAccessException {

        Contract contract = new Contract();

        Employee employee = this.repository.getEmployee(helper.getEmployeeID());

        // inherited from document
        contract.setEmployee(employee);
        contract.setNameAtSigning(employee.getFirstName() + " " + employee.getLastName());
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

        String email = jwtUtil.extractUsernameFromRaw(token);
        this.repository.createContract(contract, email);
    }

    // ---------- DAYS OFF REQUEST ----------

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "/documents/create/daysoff")
    public void createDaysOffRequest(
            @RequestBody AddDaysOffRequestHelper helper,
            @RequestHeader("Authorization") String token) throws IllegalAccessException
    {
        DaysOffRequest request = new DaysOffRequest();

        Employee employee = this.repository.getEmployee(helper.getEmployeeID());

        // inherited from document
        request.setEmployee(employee);
        request.setNameAtSigning(employee.getFirstName() + " " + employee.getLastName());
        request.setDateIssued(LocalDate.now());
        request.setSigned(false);
        request.setDateFrom(helper.getDateFrom());
        request.setDateTo(helper.getDateTo());

        // own
        request.setLeaveType(helper.getLeaveType());

        this.repository.createDaysOffRequest(request, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "/documents/update/daysoff/{documentID}")
    public void updateDaysOffRequest(
            @PathVariable long documentID,
            @RequestBody AddDaysOffRequestHelper addDaysOffRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.repository.updateDaysOffRequest(documentID, addDaysOffRequestHelper, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/documents/delete/daysoff/{documentID}")
    public void removeDaysOffRequest(
            @PathVariable long documentID,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.repository.removeDaysOffRequest(documentID, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents/daysoff")
    public List<DaysOffRequest> getAllDaysOffRequests(@RequestHeader("Authorization") String token){
        return this.repository.getAllDaysOffRequests();
    }

    // ---------- DELEGATION REQUEST ----------
    // TODO - the person making the request should be the one the request is hooked up to
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @PostMapping(value = "/documents/create/delegation")
    public void createDelegationRequest(
            @RequestBody AddDelegationRequestHelper helper,
            @RequestHeader("Authorization") String token
    ) {
        DelegationRequest request = new DelegationRequest();

        Employee employee = this.repository.getEmployee(helper.getEmployeeID());

        // inherited from document
        request.setEmployee(employee);
        request.setNameAtSigning(employee.getFirstName() + " " + employee.getLastName());
        request.setDateIssued(LocalDate.now());
        request.setSigned(false);
        request.setDateFrom(helper.getDateFrom());
        request.setDateTo(helper.getDateTo());

        // own
        request.setDestination(helper.getDestination());

        this.repository.createDelegationRequest(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @PostMapping(value = "/documents/update/delegation/{documentID}")
    public void updateDelegationRequest(
            @PathVariable long documentID,
            @RequestBody AddDelegationRequestHelper delegationRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.repository.updateDelegationRequest(documentID, delegationRequestHelper, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_EMPLOYEE')")
    @DeleteMapping(value = "/documents/delete/delegation/{documentID}")
    public void removeDelegationRequest(
            @PathVariable long documentID,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.repository.removeDelegationRequest(documentID, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents/delegation")
    public List<DelegationRequest> getAllDelegationRequests(){
        return this.repository.getAllDelegationRequests();
    }


    // ---------- EMPLOYEE ----------

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/employees/{id}")
    public Employee getEmployeeById(
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        return this.repository.getEmployee(id, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/create")
    public void createEmployee(@RequestBody AddEmployeeHelper helper) {
        this.repository.createEmployee(helper);
        }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employees/{id}")
    public void removeEmployee(@PathVariable long id) {
        this.repository.removeEmployee(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/{employeeId}/edit/data")
    @ResponseBody
    public Employee updateEmployeeData(
            @PathVariable long employeeId,
            @RequestBody Employee updatedEmployee,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        //TODO send EmployeeHelper in json (or new structure because we don't save permissions in this method)
        Employee currentEmployee = repository.getEmployee(employeeId);
        currentEmployee.setFirstName(updatedEmployee.getFirstName());
        currentEmployee.setLastName(updatedEmployee.getLastName());
        currentEmployee.setEmail(updatedEmployee.getEmail());
        currentEmployee.setPhone(updatedEmployee.getPhone());
        currentEmployee.setAddress(updatedEmployee.getAddress());

        return repository.updateEmployee(currentEmployee, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/permissions")
    @ResponseBody
    public Employee updateEmployeePermissions(
            @PathVariable long employeeId,
            @RequestBody Permissions updatedPermissions,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        //TODO send PermissionHelper in json
        Employee employee = repository.getEmployee(employeeId);
        employee.getPermissions().setManagedUsers(updatedPermissions.getManagedUsers());
        employee.getPermissions().setModifyBonusBudget(updatedPermissions.isModifyBonusBudget());
        employee.setPermissions(updatedPermissions);

        return repository.updateEmployee(employee, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/subordinates")
    @ResponseBody
    public Employee updateSubordinatesOfEmployee(
            @PathVariable long employeeId,
            @RequestBody long[] subordinates,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        //TODO fix adding subordinates - saving them to the managed users table
        Employee employee = repository.getEmployee(employeeId);
        List<Employee> managedUsers = new ArrayList<>();

        for (long l : subordinates) {
            Employee subordinate = repository.getEmployee(l);
            managedUsers.add(subordinate);
        }

        employee.getPermissions().setManagedUsers(managedUsers);
        return repository.updateEmployee(employee, jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/getAbsent/{from}/{to}")
    public List<AbsentEmployees> getAbsentEmployees(
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getAbsentEmployees(LocalDate.parse(from, formatter), LocalDate.parse(to, formatter), jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/{id}/documents/delegation/{from}/{to}")
    public List<DelegationRequest> getEmployeeDelegationRequests(
            @PathVariable long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDelegationRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter), jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/{id}/documents/daysoff/{from}/{to}")
    public List<DaysOffRequest> getEmployeeDaysOffRequests(
            @PathVariable long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDaysOffRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter), jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/getDaysOff/{employeeId}")
    public int getEmployeesDaysOffLeft(
            @PathVariable long employeeId,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        return this.repository.getEmployeesDaysOffLeft(employeeId, jwtUtil.extractUsernameFromRaw(token));
    }
}
