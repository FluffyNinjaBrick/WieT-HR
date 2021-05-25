package com.wiethr.app.controller;


import com.itextpdf.text.DocumentException;
import com.wiethr.app.model.*;
import com.wiethr.app.model.helpers.*;
import com.wiethr.app.repository.GeneratePDF;
import com.wiethr.app.repository.IWietHRRepository;
import com.wiethr.app.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestController {

    private final IWietHRRepository repository;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailService userDetailService;
    private final JwtUtil jwtUtil;
    private final RoleValidator roleValidator;

    @Autowired
    public RestController(
            IWietHRRepository repository,
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
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
//            throw new Exception("Incorrect email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }
        final MyUserDetails userDetails = (MyUserDetails) this.userDetailService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt, userDetails.getUserRole(), userDetails.getId()));
    }



    // ---------- APPRECIATION BONUS ----------
    @GetMapping("/bonuses")
    public List<AppreciationBonus> getBonuses(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        return this.repository.getEmployeeBonuses(id);
    }


    @PutMapping("/bonuses")
    public void addAppreciationBonus(
            @RequestHeader("Authorization") String token,
            @RequestBody AddAppreciationBonusHelper bonusHelper
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), bonusHelper.getEmployeeId());
        this.repository.addAppreciationBonus(bonusHelper);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/bonuses/{year}")
    public BonusesOfAllEmployeesHelper getBonusesForYear(@PathVariable Year year) {
        return this.repository.getBonusesForYear(year);
    }

    // ---------- BONUS BUDGET ----------
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/budget")
    public void createBonusBudget(@RequestBody BonusBudgetHelper helper) {
        this.repository.createBonusBudget(helper);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/budget")
    public BonusBudget getBudgetForYear(@RequestParam Year year) {
        return this.repository.getBudgetForYear(year);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/budget")
    public void modifyBonusBudget(@RequestBody BonusBudgetHelper helper) {
        this.repository.modifyBonusBudget(helper);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/budget/{year}")
    public BonusBudgetHelper getBonusBudgetForYear(@PathVariable Year year) {

        BonusBudget budget = this.repository.getBudgetForYear(year);

        return new BonusBudgetHelper(
                budget.getId(),
                budget.getYear(),
                budget.getValue(),
                repository.getBonusBudgetLeft(budget),
                repository.getBonusBudgetUsagePerMonth(budget)
        );
    }


    // ---------- CONTRACT ----------
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    @PostMapping(value = "/contracts")
    public void createContract(
            @RequestBody AddContractHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, helper.getEmployeeID());
        this.repository.createContract(helper);
    }


    @GetMapping(value = "/contracts")
    public List<Contract> getAvailableContracts(@RequestHeader("Authorization") String token){
        String email = jwtUtil.extractUsernameFromRaw(token);
        return this.repository.getAvailableContracts(email);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PutMapping(value = "/contract_annex/sign")
    public void signContractAnnex(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getContractById(id).getEmployee());
        this.repository.signContractAnnex(id, email);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping(value = "/contracts/annexes")
    public void createAnnex(
            @RequestBody AddAnnexHelper addAnnexHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, addAnnexHelper.getEmployeeId());
        this.repository.createAnnex(addAnnexHelper);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/contract_annex")
    public void deleteContractAnnex(@RequestParam long id, @RequestHeader("Authorization") String token) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getContractById(id).getEmployee());
        this.repository.deleteContractAnnex(id);
    }


    // ---------- DAYS OFF REQUEST ----------
    @PostMapping(value = "/daysoff")
    public void createDaysOffRequest(
            @RequestBody DaysOffRequestHelper helper,
            @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.repository.createDaysOffRequest(helper, email);
    }


    @PutMapping(value = "/daysoff")
    public void updateDaysOffRequest(
            @RequestBody DaysOffRequestHelper daysOffRequestHelper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        DaysOffRequest request = this.repository.getDaysOffRequestByID(daysOffRequestHelper.getDocumentId());
        this.roleValidator.validate(email, request.getEmployee());
        this.repository.updateDaysOffRequest(daysOffRequestHelper, request.employeeObject());
    }


    @DeleteMapping(value = "/daysoff")
    public void removeDaysOffRequest(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getDaysOffRequestByID(id).getEmployee());
        this.repository.removeDaysOffRequest(id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/daysoff")
    public List<DaysOffRequest> getAllDaysOffRequests(@RequestHeader("Authorization") String token) {
        return this.repository.getAllDaysOffRequests();
    }


    @GetMapping(value = "/daysoff/pdf")
    public ResponseEntity<byte[]> getDaysOffRequestPDF(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws DocumentException, IllegalAccessException {
        DaysOffRequest request = this.repository.getDaysOffRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        return GeneratePDF.fromDaysOffRequest(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping(value = "/daysoff/sign")
    public void signDaysOffRequest(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        DaysOffRequest request = this.repository.getDaysOffRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        this.repository.signDaysOffRequest(id, email);
    }


    // ---------- DELEGATION REQUEST ----------
    @PostMapping(value = "/delegation")
    public void createDelegationRequest(
            @RequestBody DelegationRequestHelper helper,
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.repository.createDelegationRequest(helper, email);
    }


    @PutMapping(value = "/delegation")
    public void updateDelegationRequest(
            @RequestBody DelegationRequestHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        DelegationRequest request = this.repository.getDelegationRequestByID(helper.getDocumentId());
        this.roleValidator.validate(email, request.getEmployee());
        this.repository.updateDelegationRequest(helper, request.employeeObject());
    }


    @DeleteMapping(value = "/delegation")
    public void removeDelegationRequest(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, this.repository.getDelegationRequestByID(id).getEmployee());
        this.repository.removeDelegationRequest(id);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/delegation")
    public List<DelegationRequest> getAllDelegationRequests() {
        return this.repository.getAllDelegationRequests();
    }


    @GetMapping(value = "/delegation/pdf")
    public ResponseEntity<byte[]> getDelegationRequestPDF(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws DocumentException, IllegalAccessException {
        DelegationRequest request = this.repository.getDelegationRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        return GeneratePDF.fromDelegationRequest(request);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping(value = "/delegation/sign")
    public void signDelegationRequest(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        DelegationRequest request = this.repository.getDelegationRequestByID(id);
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, request.getEmployee());
        this.repository.signDelegationRequest(id, email);
    }



    // ---------- EMPLOYEE ----------
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/employees")
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }


    @GetMapping(value = "/employee")
    public Employee getEmployeeById(
            @RequestParam long id,
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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employees")
    public void removeEmployee(@RequestParam long id) {
        this.repository.removeEmployee(id);
    }


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


    @PutMapping("/employees/password")
    public void updatePassword(
            @RequestBody UpdatePasswordHelper helper,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validatePasswordUpdate(jwtUtil.extractUsernameFromRaw(token), helper.getId());
        this.repository.updatePassword(helper);
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


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/delegations/{from}/{to}")
    public List<DelegationRequest> getEmployeeDelegationRequests(
            @RequestParam long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDelegationRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff/{from}/{to}")
    public List<DaysOffRequest> getEmployeeDaysOffRequests(
            @RequestParam long id,
            @PathVariable String from,
            @PathVariable String to,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.repository.getEmployeeDaysOffRequests(id, LocalDate.parse(from, formatter), LocalDate.parse(to, formatter));
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff")
    public EmployeeDaysOffDetails getEmployeeDaysOffLeft(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        this.roleValidator.validate(jwtUtil.extractUsernameFromRaw(token), id);
        return this.repository.getEmployeeDaysOffLeft(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/employees/daysoff/all")
    public GroupDaysOffDetails getGroupDaysOffLeft(
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        return this.repository.getGroupDaysOffLeft(jwtUtil.extractUsernameFromRaw(token));
    }


    @GetMapping("/employees/salaries/{year}")
    public EmployeesSalariesHelper getSalaries(
            @RequestHeader("Authorization") String token,
            @PathVariable int year
    ) {
        return this.repository.getSalaries(year, jwtUtil.extractUsernameFromRaw(token));
    }

    @GetMapping(value = "/employees/contract")
    public Contract getCurrentContractForEmployee(
            @RequestParam long id,
            @RequestHeader("Authorization") String token
    ) throws IllegalAccessException {
        String email = jwtUtil.extractUsernameFromRaw(token);
        this.roleValidator.validate(email, id);
        return this.repository.getCurrentContractForEmployee(id);
    }


}
