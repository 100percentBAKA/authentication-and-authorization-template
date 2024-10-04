package com.example.security_demo.controllers;

import com.example.security_demo.dtos.ResponseMessageDTO;
import org.springframework.web.bind.annotation.*;

import com.example.security_demo.dtos.JwtResponseDTO;
import com.example.security_demo.dtos.LoginDTO;
import com.example.security_demo.models.User;
import com.example.security_demo.services.CustomUserDetailsService;
import com.example.security_demo.services.JWTService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AuthControllers {

    // @Autowired
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthControllers(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO body, BindingResult result, HttpServletResponse response) {
        try {
            if(result.hasErrors()) {
                return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                body.getEmail(), // this is the principal (username)
                body.getPassword() // this is the credential (password)
                // roles will have to be passed here
            );

            Authentication authObject = authenticationManager.authenticate(token); // get the auth object 
            SecurityContextHolder.getContext().setAuthentication(authObject); // provide the auth obj to security context

            // now the auth obj is available to the security filter chain

            // returning the refresh and access tokens
            String accessToken = jwtService.generateToken(body.getEmail(), 1000 * 60 * 1); // 10 mins
            String refreshToken = jwtService.generateToken(body.getEmail(), 1000 * 60 * 60 * 24 * 7); // 7 days
            
            // ! OLD METHOD

            // Map<String, String> tokens = new HashMap<>(); 
            // tokens.put("accessToken", accessToken);
            // tokens.put("refreshToken", refreshToken);

            // return new ResponseEntity<>(tokens, HttpStatus.OK);

            // ! NEW METHOD

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true); 
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(1000 * 60 * 60 * 24 * 7); // 7 days

            response.addCookie(refreshTokenCookie);

            // now return the JwtResponseDTO
            
            // ! before doing that lets get the list of all user roles
            List<String> roles = authObject.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
            
            return new ResponseEntity<>(new JwtResponseDTO(accessToken, roles), HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody User body, BindingResult result) {
//        if (result.hasErrors()) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//
//        // validation check done
//
//        if (customUserDetailsService.existsByEmail(body.getEmail())) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Email is already taken!");
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
//
//        // email check done
//
//        User newUser = new User();
//        newUser.setEmail(body.getEmail());
//        newUser.setPassword(passwordEncoder.encode(body.getPassword()));
//        newUser.setFirstName(body.getFirstName());
//        newUser.setLastName(body.getLastName());
//        newUser.setUserAuthority(body.getUserAuthority());
//
//        customUserDetailsService.addUser(newUser);
//
//        Map<String, String> successResponse = new HashMap<>();
//        successResponse.put("message", "User registered successfully");
//
//        return new ResponseEntity<>(successResponse, HttpStatus.OK);
//    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessageDTO> register(@Valid @RequestBody User body, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO(result.getFieldError().getDefaultMessage()));
        }

        if (customUserDetailsService.existsByEmail(body.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessageDTO("Email is already taken!"));
        }

        User newUser = new User();
        newUser.setEmail(body.getEmail());
        newUser.setPassword(passwordEncoder.encode(body.getPassword()));
        newUser.setFirstName(body.getFirstName());
        newUser.setLastName(body.getLastName());
        newUser.setUserAuthority(body.getUserAuthority());

        customUserDetailsService.addUser(newUser);

        return ResponseEntity.ok(new ResponseMessageDTO("User registered successfully"));
    }


    // @PostMapping("/refresh")
    // public ResponseEntity<?> getAccessToken(@RequestBody String refreshToken) {
    //     try {
    //         if (refreshToken == null || refreshToken.isEmpty()) {
    //             return new ResponseEntity<>("Refresh token is missing", HttpStatus.BAD_REQUEST);
    //         }

    //         String username = jwtService.extractUsername(refreshToken);
    //         if (username == null || username.isEmpty()) {
    //             return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    //         }

    //         UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
    //         if (userDetails == null || !jwtService.validateToken(refreshToken, userDetails)) {
    //             return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    //         }

    //         String newAccessToken = jwtService.generateToken(username, 1000 * 60 * 15);

    //         Map<String, String> tokens = Map.of(
    //             "accessToken", newAccessToken
    //         );

    //         return new ResponseEntity<>(tokens, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>("An error occurred while processing the refresh token", HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    // ! SOME WAYS TO USE COOKIES

    // @GetMapping("/")
    // public String readCookie(@CookieValue(value = "username", defaultValue = "Atta") String username) {
    //     return "Hey! My username is " + username;
    // }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                                    .findFirst()
                                    .map(Cookie::getValue)
                                    .orElse(null);

        if (refreshToken != null) {
            String username = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // ! here it is necessary to check for the validity of the refresh token also,
            // ! if the refresh token is no longer valid, the user must re authenticate

            if (jwtService.validateToken(refreshToken, userDetails)) {
                String newAccessToken = jwtService.generateToken(username, 1000 * 60 * 1);
                return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token, the user must re-login");
    }

    // @PostMapping("/refresh-token")
    // public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken") String refreshToken) {
    //     System.out.println("----------------------------------" + refreshToken + "----------------------------------");

    //     if (refreshToken != null) {
    //         String username = jwtService.extractUsername(refreshToken);
    //         UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

    //         // ! here it is necessary to check for the validity of the refresh token also,
    //         // ! if the refresh token is no longer valid, the user must re authenticate

    //         if (jwtService.validateToken(refreshToken, userDetails)) {
    //             String newAccessToken = jwtService.generateToken(username, 1000 * 60 * 1);
    //             return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    //         }
    //     }

    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token, the user must re-login");
    // }

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles() {
        // ! This example endpoint can be very crucial to understand, how the security context object works behind the scenes 

        // ! the security context is set with the authentication obj, in the jwt filter

        // ! only because of the above feature, we are able to get the roles of the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roles);
    }
    
}
