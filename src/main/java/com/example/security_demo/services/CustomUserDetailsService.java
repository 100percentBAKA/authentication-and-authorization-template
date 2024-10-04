package com.example.security_demo.services;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security_demo.models.User;
import com.example.security_demo.repositories.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        List<GrantedAuthority> list = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getUserAuthority().name()) // name() convert enum --> String
        );

        // ! we can also return a new UserPrincipal(), as it implements the UserDetails interface
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            list
        );
    }
    
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    public void addUser(User user) {
        userRepo.save(user);
    }
}
