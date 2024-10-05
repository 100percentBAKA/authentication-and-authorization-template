//package com.example.security_demo.config;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//import io.jsonwebtoken.JwtException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.example.security_demo.services.CustomUserDetailsService;
//import com.example.security_demo.services.JWTService;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//    private final JWTService jwtService;
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public JwtFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
//        this.jwtService = jwtService;
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException
//    {
//        // ! OLD METHOD
//        // String authHeader = request.getHeader("Authorization");
//        // String token = null;
//        // String username = null;
//
//        // if(authHeader != null && authHeader.startsWith("Bearer ")) {
//        //     token = authHeader.substring(7);
//        //     username = jwtService.extractUsername(token);
//        // }
//
//        // ! NEW METHOD
////        Cookie[] cookies = request.getCookies();
////        String username = null;
////        String accessToken = null;
////
////        if(cookies != null) {
////            accessToken = Arrays.stream(cookies)
////                    .filter(cookie -> "accessToken".equals(cookie.getName()))
////                    .findFirst()
////                    .map(Cookie::getValue)
////                    .orElse(null);
////        }
////
////
////        if(accessToken != null) {
////            username = jwtService.extractUsername(accessToken);
////        }
////
////        // ! check if the token is valid (ie not expired), not expired got to next condition, if expired perform refreshing of the token and update the access token
////        if(jwtService.validateToken(accessToken)) {
////            // the token has expired
////            Cookie[] refreshCookie = request.getCookies();
////            String refreshToken = Arrays.stream(refreshCookie)
////                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
////                    .findFirst()
////                    .map(Cookie::getValue)
////                    .orElse(null);
////
////            if (refreshToken != null) {
////                String checkUsername = jwtService.extractUsername(refreshToken);
////                UserDetails userDetails = customUserDetailsService.loadUserByUsername(checkUsername);
////
////                // ! here it is necessary to check for the validity of the refresh token also,
////                // ! if the refresh token is no longer valid, the user must re authenticate
////
////                if (jwtService.validateToken(refreshToken, userDetails)) {
////                     accessToken = jwtService.generateToken(username, 1000 * 60 * 1);
////                }
////            }
////        }
////
////        // ! we have got the username, use this username to obtain userDetails from the customUserImpl (ref service package)
////        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////            UserDetails user = customUserDetailsService.loadUserByUsername(username);
////
////            if(jwtService.validateToken(accessToken, user)) {
////                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
////
////                authToken.setDetails(new WebAuthenticationDetailsSource()
////                        .buildDetails(request));
////                SecurityContextHolder.getContext().setAuthentication(authToken);
////            }
////        }
//
//
//         try {
//            Cookie[] cookies = request.getCookies();
//
//            if(cookies == null) {
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            String accessToken = Arrays.stream(cookies)
//                    .filter(cookie -> cookie.getName().equals("accessToken"))
//                    .findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);
//
//            String refreshToken = Arrays.stream(cookies)
//                    .filter(cookie -> cookie.getName().equals("refreshToken"))
//                    .findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);
//
//            if(accessToken == null && refreshToken == null) {
//                throw new JwtException("JWT tokens corrupted, user needs to re-login");
//            }
//
//            String usernameAccessToken = null;
//            String usernameRefreshToken = null;
//
//            if(accessToken != null) {
//                usernameAccessToken = jwtService.extractUsername(accessToken);
//            }
//
//            if(refreshToken != null) {
//                usernameRefreshToken = jwtService.extractUsername(refreshToken);
//            }
//
//             if (usernameRefreshToken == null || !usernameRefreshToken.equals(usernameAccessToken)) {
//                 throw new JwtException("JWT tokens corrupted, user needs to re-login");
//             }
//
//             // ! CHECK FOR REFRESH TOKENS, IF INVALID, ASK CLIENT TO RE LOGIN, PERFORM SESSION CLEAN ON THE SERVER
//             if(!jwtService.validateToken(refreshToken)) {
//                 // THEN CHECK IF THE TOKEN BELONGS TO THE CURRENT AUTHENTICATED USE
//                 UserDetails user = customUserDetailsService.loadUserByUsername(usernameRefreshToken);
//
//                 if(jwtService.validateToken(refreshToken, user)) {
//                     // THE TOKEN BELONGS TO THE USER
//                     if(jwtService.validateToken(accessToken)) {
//                         // AND IF THE ACCESS TOKENS ARE EXPIRED
//                         accessToken = jwtService.generateToken(usernameRefreshToken, 1000 * 60 * 1); // 10 MINS
//
//                         Cookie newAccessTokenCookie = new Cookie("accessToken", accessToken);
//                         newAccessTokenCookie.setHttpOnly(true);
//                         newAccessTokenCookie.setSecure(true);
//                         newAccessTokenCookie.setPath("/");
//                         newAccessTokenCookie.setMaxAge(1000 * 60 * 60 * 24 * 7);
//
//                         response.addCookie(newAccessTokenCookie);
//                     }
//
//                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                     authToken.setDetails(new WebAuthenticationDetailsSource()
//                        .buildDetails(request));
//                     SecurityContextHolder.getContext().setAuthentication(authToken);
//                 }
//                 else {
//                     throw new JwtException("JWT tokens corrupted, user needs to re-login");
//                 }
//
//             }
//             else {
//                 throw new JwtException("JWT tokens corrupted, user needs to re-login");
//             }
//         }
//         catch (Exception e) {
//             SecurityContextHolder.clearContext();
//             response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//             response.setContentType("application/json");
//
//             // Create a JSON response with the error message
//             String jsonResponse = String.format("{\"message\": \"An unexpected error occurred. Please try again later. %s\"}", e.getLocalizedMessage());
//
//             response.getWriter().write(jsonResponse);
//             return;
//         }
//
//        filterChain.doFilter(request, response);
//    }
//
//}

package com.example.security_demo.config;

import java.io.IOException;
import java.util.Arrays;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.security_demo.services.CustomUserDetailsService;
import com.example.security_demo.services.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = Arrays.stream(cookies)
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            String refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (accessToken == null && refreshToken == null) {
                throw new JwtException("JWT tokens missing, user needs to re-login");
            }

            String username = null;

            if (accessToken != null) {
                try {
                    username = jwtService.extractUsername(accessToken);
                } catch (JwtException e) {
                    // Invalid access token, try to refresh it if refresh token is available
                    accessToken = null;
                }
            }

            if (username == null && refreshToken != null) {
                username = jwtService.extractUsername(refreshToken);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(refreshToken, userDetails)) {
                    // Generate new access token
                    accessToken = jwtService.generateToken(username, 1000 * 60 * 10); // 10 minutes

                    Cookie newAccessTokenCookie = new Cookie("accessToken", accessToken);
                    newAccessTokenCookie.setHttpOnly(true);
                    newAccessTokenCookie.setSecure(true);
                    newAccessTokenCookie.setPath("/");
                    newAccessTokenCookie.setMaxAge(1000 * 60 * 60 * 24 * 7);

                    response.addCookie(newAccessTokenCookie);

                    // Set authentication
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new JwtException("Invalid refresh token, user needs to re-login");
                }
            } else if (username != null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtService.validateToken(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        catch (Exception e) {
            SecurityContextHolder.clearContext();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            String jsonResponse = String.format("{\"message\": \"An unexpected error occurred. Please try again later. %s\"}", e.getLocalizedMessage());
            response.getWriter().write(jsonResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
