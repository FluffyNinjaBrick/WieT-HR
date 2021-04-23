package com.wiethr.app.security;

import com.wiethr.app.model.Employee;
import com.wiethr.app.repository.WietHRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
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
        Employee employee = repository.getEmployeeByEmail(email);
//            user = new User(employee.getEmail(), employee.getPassword(), new ArrayList<>());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + employee.getUserRole()));
        UserDetails user = new MyUserDetails(employee.getId(), employee.getEmail(), employee.getPassword(), employee.getUserRole(), authorities);
        return user;
    }
}
