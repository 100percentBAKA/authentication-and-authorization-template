package com.example.security_demo.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTService {

    private byte[] keyGen;

    @Value("${myapp.secret-key}")
    private String secretKey; // ! do not use the final keyword here, spring is going to inject the secret key from the application.yml file

    // ! NOT RECOMMEND TO GENERATE A NEW TOKEN
//    public JWTService() {
//        try {
//            keyGen = KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

    @PostConstruct
    public void init() {
        this.keyGen = secretKey.getBytes(StandardCharsets.UTF_8);
    }

    // ? the post construct method need not always be named init
    // ? it should just return void, doesn't accept any constructor and must be annotated with @PostConstruct

    // ! The error you're encountering is due to the secretKey being null when the constructor of the JWTService is called. This happens because Spring has not yet injected the value of secretKey from the configuration file when the constructor is executed.

    // ! In Spring, when you need to use a property injected from application.yml (like myapp.secret-key in your case), it is not available in the constructor unless you use a setter or a @PostConstruct method.

    // ! JWT UTIL METHODS
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(keyGen);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
        // ! true -> the token is expired
        // ! false -> no the token is not expired
    }

    // @param time is treated as milliseconds here
    public String generateToken(String username, Integer time) {
        Map<String, Object> claims  = new HashMap<>();

        // ! setClaims() deprecated

        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * time)) 
            .and()
            .signWith(getKey())
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        return isTokenExpired(token);
    }
    
    public boolean validateToken(String token, UserDetails user) {
        final String userName = extractUsername(token);
        return (userName.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
