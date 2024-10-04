package com.example.security_demo.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtResponseDTO {
    private String accessToken;
    private final String tokenType;
    private List<String> roles;

    public JwtResponseDTO(String accessToken, List<String> roles) {
        this.accessToken = accessToken;
        this.tokenType = "bearer";
        this.roles = roles;
    }
}
