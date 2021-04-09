package com.wiethr.app.security;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {

    private String email;
    private String password;
}
