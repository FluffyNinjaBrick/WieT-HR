package com.wiethr.app.controller;


import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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


}
