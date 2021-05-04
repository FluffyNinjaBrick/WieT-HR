package com.wiethr.app.controller;


import com.itextpdf.text.DocumentException;
import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.GeneratePDF;
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

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestController {

    private final WietHRRepository repository;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailService userDetailService;
    private final JwtUtil jwtUtil;
    private final RoleValidator roleValidator;

    @Autowired
    public RestController(
            WietHRRepository repository,
            AuthenticationManager authenticationManager,
            MyUserDetailService userDetailService,
            JwtUtil jwtUtil,
            RoleValidator roleValidator) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
        this.roleValidator = roleValidator;
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
    public void createContract(
            @RequestBody AddContractHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, helper.getEmployeeID());
        this.repository.createContract(helper);
    }


    // ---------- DAYS OFF REQUEST ----------
    @PostMapping(value = "/documents/create/daysoff")
    public void createDaysOffRequest(
            @RequestBody AddDaysOffRequestHelper helper,
            @RequestHeader("Authorization") String token) throws IllegalAccessException
    {
        String email = jwtUtil.extractUsernameFromRaw(token);
        roleValidator.validate(email, helper.getEmployeeID());
        this.repository.createDaysOffRequest(helper);
    }

    @PostMapping(value = "/documents/update/daysoff/{documentID}")
    public void updateDaysOffRequest(
            @PathVariable long documentID,
            @RequestBody AddDaysOffRequestHelper addDaysOffRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, addDaysOffRequestHelper.getEmployeeID());
        this.repository.updateDaysOffRequest(documentID, addDaysOffRequestHelper);
    }

    @DeleteMapping(value = "/documents/delete/daysoff/{documentID}")
    public void removeDaysOffRequest(
            @PathVariable long documentID,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getDaysOffRequestByID(documentID).getEmployee());
        this.repository.removeDaysOffRequest(documentID);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents/daysoff")
    public List<DaysOffRequest> getAllDaysOffRequests(@RequestHeader("Authorization") String token) {
        return this.repository.getAllDaysOffRequests();
    }

    @GetMapping(value = "/documents/daysoff/pdf/{id}")
    public ResponseEntity<byte[]> getDaysOffRequestPDF(
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws DocumentException, IllegalAccessException {
        DaysOffRequest request = this.repository.getDaysOffRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        return GeneratePDF.fromDaysOffRequest(request);
    }


    // ---------- DELEGATION REQUEST ----------
    // TODO - the person making the request should be the one the request is hooked up to

    @PostMapping(value = "/documents/create/delegation")
    public void createDelegationRequest(
            @RequestBody AddDelegationRequestHelper helper,
            @RequestHeader("Authorization") String token
    ) {
        this.repository.createDelegationRequest(helper);
    }

    @PostMapping(value = "/documents/update/delegation/{documentID}")
    public void updateDelegationRequest(
            @PathVariable long documentID,
            @RequestBody AddDelegationRequestHelper delegationRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, delegationRequestHelper.getEmployeeID());
        this.repository.updateDelegationRequest(documentID, delegationRequestHelper);
    }

    @DeleteMapping(value = "/documents/delete/delegation/{documentID}")
    public void removeDelegationRequest(
            @PathVariable long documentID,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getDelegationRequestByID(documentID).getEmployee());
        this.repository.removeDelegationRequest(documentID);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/documents/delegation")
    public List<DelegationRequest> getAllDelegationRequests() {
        return this.repository.getAllDelegationRequests();
    }

    @GetMapping(value = "/documents/delegation/pdf/{id}")
    public ResponseEntity<byte[]> getDelegationRequestPDF(
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws DocumentException, IllegalAccessException {
        DelegationRequest request = this.repository.getDelegationRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        return GeneratePDF.fromDelegationRequest(request);
    }


    // ---------- EMPLOYEE ----------
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }

    @GetMapping(value = "/employees/{id}")
    public Employee getEmployeeById(
            @PathVariable long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, id);
        return this.repository.getEmployee(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/create")
    public void createEmployee(@RequestBody AddEmployeeHelper helper) {
        this.repository.createEmployee(helper);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employees/{id}")
    public void removeEmployee(@PathVariable long id) {
        this.repository.removeEmployee(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/data")
    @ResponseBody
    public Employee updateEmployeeData(
            @PathVariable long employeeId,
            @RequestBody UpdateEmployeeHelper updatedEmployee,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {

        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, employeeId);

        //TODO send return EmployeeHelper in json (or new structure because we don't save permissions in this method)
        Employee currentEmployee = repository.getEmployee(employeeId);
        currentEmployee.setFirstName(updatedEmployee.getFirstName());
        currentEmployee.setLastName(updatedEmployee.getLastName());
        currentEmployee.setEmail(updatedEmployee.getEmail());
        currentEmployee.setPhone(updatedEmployee.getPhone());
        currentEmployee.setAddress(updatedEmployee.getAddress());

        return repository.updateEmployee(currentEmployee);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/permissions")
    @ResponseBody
    public void updateEmployeePermissions(
            @PathVariable long employeeId,
            @RequestBody PermissionHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, employeeId);
        this.repository.updateEmployeePermissions(employeeId, helper);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees/{employeeId}/edit/subordinates")
    @ResponseBody
    public Employee updateSubordinatesOfEmployee(
            @PathVariable long employeeId,
            @RequestBody long[] subordinates,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        //TODO return EmployeeHelper?
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, employeeId);


        Employee employee = repository.getEmployee(employeeId);
        List<Employee> managedUsers = new ArrayList<>();

        for (long l : subordinates) {
            Employee subordinate = repository.getEmployee(l);
            managedUsers.add(subordinate);
        }

        employee.getPermissions().setManagedUsers(managedUsers);
        return repository.updateEmployee(employee);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/getAbsent/{from}/{to}")
    public List<AbsentEmployees> getAbsentEmployees(
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validateAbsent(jwtUtil.extractUsernameFromRaw(token));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getAbsentEmployees(LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/{id}/documents/delegation/{from}/{to}")
    public List<DelegationRequest> getEmployeeDelegationRequests(
            @PathVariable long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDelegationRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/{id}/documents/daysoff/{from}/{to}")
    public List<DaysOffRequest> getEmployeeDaysOffRequests(
            @PathVariable long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDaysOffRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/getDaysOff/{employeeId}")
    public EmployeeDaysOffDetails getEmployeeDaysOffLeft(
            @PathVariable long employeeId,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), employeeId);
        return this.repository.getEmployeeDaysOffLeft(employeeId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/getDaysOff/all")
    public GroupDaysOffDetails getGroupDaysOffLeft(
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        return this.repository.getGroupDaysOffLeft(jwtUtil.extractUsernameFromRaw(token));
    }

}
