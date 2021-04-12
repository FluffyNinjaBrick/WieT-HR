package com.wiethr.app.security;


import com.wiethr.app.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String jwt;
    private final UserRole userRole;
    private final long id;
}
