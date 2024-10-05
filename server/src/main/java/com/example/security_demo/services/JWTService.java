package com.example.security_demo.services;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private final byte[] keyGen;

    // ! NOT RECOMMEND TO GENERATE A NEW TOKEN
//    public JWTService() {
//        try {
//            keyGen = KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

    public JWTService() {
        this.keyGen = "daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb".getBytes(StandardCharsets.UTF_8);
    }

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
    }

    public String generateToken(String username, long time) {
        Map<String, Object> claims  = new HashMap<>();

        // ! setClaims() deprecated

        return Jwts.builder()
            .claims()
            .add(claims)
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + time)) // ! time --> 60 * 60 * 30
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
