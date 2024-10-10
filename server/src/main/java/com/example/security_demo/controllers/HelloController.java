package com.example.security_demo.controllers;

import com.example.security_demo.dtos.ResponseMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
    
    @GetMapping("/public")
    public String getPublicMethod() {
        return new String("This is free for everyone");
    }

    @GetMapping("/private")
    public ResponseEntity<?> getPrivateMethod() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDTO("This is only for paid users and admin"));
    }

    @GetMapping("/admin")
    public String getAdminMethod() {
        return new String("This is only for the admin, if you are not admin, please leave 😢");
    }
}
