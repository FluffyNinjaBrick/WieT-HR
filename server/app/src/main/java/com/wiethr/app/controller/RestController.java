package com.wiethr.app.controller;


import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.WietHRRepository;
import com.wiethr.app.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping(value = "/documents/create/contract")
    public void createContract(@RequestBody AddContractHelper helper) {

        Contract contract = new Contract();

        Employee employee = this.repository.getEmployee(helper.getEmployeeID()).get();

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
    @PostMapping(value = "/documents/create/daysoff")
    public void createDaysOffRequest(
            @RequestBody AddDaysOffRequestHelper helper,
            @RequestHeader("Authorization") String token) throws IllegalAccessException
    {
        DaysOffRequest request = new DaysOffRequest();

        Employee employee = this.repository.getEmployee(helper.getEmployeeID()).get();

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

        Employee employee = this.repository.getEmployee(helper.getEmployeeID()).get();

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
    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }

    @PostMapping("/employees/create")
    public void createEmployee(@RequestBody AddEmployeeHelper helper) {
        this.repository.createEmployee(helper);
    }

    @DeleteMapping("/employees/{id}")
    public void removeEmployee(@PathVariable long id) {
        this.repository.removeEmployee(id);
    }

    @PostMapping("/employees/{employeeId}/edit/data")
    @ResponseBody
    public Employee updateEmployeeData(@PathVariable long employeeId, @RequestBody Employee updatedEmployee) {

        Optional<Employee> currentEmployeeOptional = repository.getEmployee(employeeId);

        if (currentEmployeeOptional.isPresent()) {

            Employee currentEmployee = currentEmployeeOptional.get();
            currentEmployee.setFirstName(updatedEmployee.getFirstName());
            currentEmployee.setLastName(updatedEmployee.getLastName());
            currentEmployee.setEmail(updatedEmployee.getEmail());
            currentEmployee.setPhone(updatedEmployee.getPhone());
            currentEmployee.setAddress(updatedEmployee.getAddress());

            return repository.updateEmployee(currentEmployee);
        }

        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/permissions")
    @ResponseBody
    public Employee updateEmployeePermissions(@PathVariable long employeeId, @RequestBody Permissions updatedPermissions) {
        Optional<Employee> employeeOptional = repository.getEmployee(employeeId);

        if (employeeOptional.isPresent()) {

            Employee employee = employeeOptional.get();
            employee.getPermissions().setManagedUsers(updatedPermissions.getManagedUsers());
            employee.getPermissions().setModifyBonusBudget(updatedPermissions.isModifyBonusBudget());
            employee.setPermissions(updatedPermissions);

            return repository.updateEmployee(employee);
        }

        return null;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
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
            return repository.updateEmployee(employee);
        }

        return null;
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
