package com.wiethr.app.controller;


import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public List<Employee> getAllEmployees() {
        return this.repository.getAllEmployees();
    }


}
