package com.example.security_demo.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LogoutService implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // ! user can be logged in (cookies are present)
        // ! user is not logged in and send a logout request

        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return; // ! un-authenticated user removed
        }

        for(Cookie cookie : cookies) {
            if
            (
                "accessToken".equals(cookie.getName()) || 
                "refreshToken".equals(cookie.getName()) || 
                "userEmail".equals(cookie.getName()) || 
                "userRoles".equals(cookie.getName()) 
            )
             {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
             }
        }

        // ! the context of the security context holder must be cleared

        // ? but it can be cleared here or in upon successful logout (I have done the latter)
    }
    
}
