package com.wiethr.app.controller;


import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT)
    public Employee updateEmployeeData(@PathVariable int id) {

    }

}
