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
    @PostMapping(value = "/documents/contract")
    public void createContract(
            @RequestBody AddContractHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, helper.getEmployeeID());
        this.repository.createContract(helper);
    }


    // ---------- DAYS OFF REQUEST ----------
    @PostMapping(value = "/documents/daysoff")
    public void createDaysOffRequest(
            @RequestBody DaysOffRequestHelper helper,
            @RequestHeader("Authorization") String token) throws IllegalAccessException
    {
        String email = jwtUtil.extractUsernameFromRaw(token);
        roleValidator.validate(email, helper.getEmployeeID());
        this.repository.createDaysOffRequest(helper);
    }

    // zmienna z path w DaysOffRequestHelper i post na put
    @PutMapping(value = "/documents/daysoff")
    public void updateDaysOffRequest(
            @RequestBody DaysOffRequestHelper daysOffRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, daysOffRequestHelper.getEmployeeID());
        this.repository.updateDaysOffRequest(daysOffRequestHelper);
    }

    // id do body
    @DeleteMapping(value = "/documents/daysoff")
    public void removeDaysOffRequest(
            @RequestBody long documentID,
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

    // id do body
    @GetMapping(value = "/documents/daysoff/pdf")
    public ResponseEntity<byte[]> getDaysOffRequestPDF(
            @RequestBody long id,
            @RequestHeader("Authorization") String token
    ) throws DocumentException, IllegalAccessException {
        DaysOffRequest request = this.repository.getDaysOffRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        return GeneratePDF.fromDaysOffRequest(request);
    }


    // ---------- DELEGATION REQUEST ----------
    // TODO - the person making the request should be the one the request is hooked up to

    //zmieniony endpoint
    @PostMapping(value = "/documents/delegation")
    public void createDelegationRequest(
            @RequestBody DelegationRequestHelper helper,
            @RequestHeader("Authorization") String token
    ) {
        this.repository.createDelegationRequest(helper);
    }

    // post na put, usuniecie update ze sciezki, documentId w helperze
    @PutMapping(value = "/documents/delegation")
    public void updateDelegationRequest(
            @RequestBody DelegationRequestHelper delegationRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, delegationRequestHelper.getEmployeeID());
        this.repository.updateDelegationRequest(delegationRequestHelper);
    }

    // id do request body, metoda zmieniona na delete
    @DeleteMapping(value = "/documents/delegation")
    public void removeDelegationRequest(
            @RequestBody long documentID,
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

    // id do body
    @GetMapping(value = "/documents/delegation/pdf")
    public ResponseEntity<byte[]> getDelegationRequestPDF(
            @RequestBody long id,
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

    // id do body, employee zamiast employees
    @GetMapping(value = "/employee")
    public Employee getEmployeeById(
            @RequestBody long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, id);
        return this.repository.getEmployee(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/employees")
    public void createEmployee(@RequestBody AddEmployeeHelper helper) {
        this.repository.createEmployee(helper);
    }

    // id do body, zmieniony endpoint
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employees")
    public void removeEmployee(@RequestBody long id) {
        this.repository.removeEmployee(id);
    }

    // post na put, zmieniony endpoint
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/employees/data")
    @ResponseBody
    public void updateEmployeeData(
            @RequestBody UpdateEmployeeHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, helper.getId());
        repository.updateEmployee(helper);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/employees/permissions")
    @ResponseBody
    public void updateEmployeePermissions(
            @RequestBody PermissionHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, helper.getEmployeeId());
        this.repository.updateEmployeePermissions(helper);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/absent/{from}/{to}")
    public List<AbsentEmployees> getAbsentEmployees(
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validateAbsent(jwtUtil.extractUsernameFromRaw(token));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getAbsentEmployees(LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }

    // id do body, zmieniony endpoint
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/{id}/documents/delegation/{from}/{to}")
    public List<DelegationRequest> getEmployeeDelegationRequests(
            @RequestBody long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDelegationRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }

    // id do body, zmieniony endpoint
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff/{from}/{to}")
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

    // inny endpoint
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff")
    public EmployeeDaysOffDetails getEmployeeDaysOffLeft(
            @RequestBody long employeeId,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), employeeId);
        return this.repository.getEmployeeDaysOffLeft(employeeId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff/all")
    public GroupDaysOffDetails getGroupDaysOffLeft(
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        return this.repository.getGroupDaysOffLeft(jwtUtil.extractUsernameFromRaw(token));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/employees/bonuses")
    public List<AppreciationBonus> getBonuses(
            @RequestHeader("Authorization") String token,
            @RequestBody long employeeID
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), employeeID);
        return this.repository.getEmployeeBonuses(employeeID);
    }
}
