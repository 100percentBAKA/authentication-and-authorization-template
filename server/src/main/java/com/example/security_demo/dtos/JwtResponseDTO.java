package com.example.security_demo.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponseDTO {
    private String username;
    private List<String> roles;

    public JwtResponseDTO(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
