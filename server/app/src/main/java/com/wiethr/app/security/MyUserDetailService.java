package com.wiethr.app.security;

import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.IWietHRRepository;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final WietHRRepository repository;

    @Autowired
    public MyUserDetailService(WietHRRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> employeeOptional = repository.getEmployeeByEmail(email);
        User user = null;
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            user = new User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
        }
        return user;
    }
}
