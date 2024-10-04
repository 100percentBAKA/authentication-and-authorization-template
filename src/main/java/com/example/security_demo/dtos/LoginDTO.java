package com.example.security_demo.dtos;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginDTO {
    @Email
    private String email;
    
    private String password;
}
