package com.example.security_demo.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
    
    @GetMapping("/public")
    public String getPublicMethod() {
        return new String("This is free for everyone");
    }

    @GetMapping("/private")
    public String getPrivateMethod() {
        return new String("This is only for paid users 🤫");
    }

    @GetMapping("/admin")
    public String getAdminMethod() {
        return new String("This is only for the admin, if you are not admin, please leave 😢");
    }
}
