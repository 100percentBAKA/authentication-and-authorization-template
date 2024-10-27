package com.example.security_demo.controllers;

import com.example.security_demo.dtos.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
    
    @GetMapping("/public")
    public ResponseEntity<?> getPublicMethod() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponseDTO<>("This endpoint is publicly available")
        );
    }

    @GetMapping("/private")
    public ResponseEntity<?> getPrivateMethod() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponseDTO<>("This endpoint is available only for pro users")
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAdminMethod() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponseDTO<>("This endpoint is available to the admin")
        );
    }
}
