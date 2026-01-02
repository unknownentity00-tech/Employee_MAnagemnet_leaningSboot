package com.Learning.Employee_Management.service;

import com.Learning.Employee_Management.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    // Pulls the "Salt" from application.properties
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    /**
     * Step 2: Adds the Salt
     * Converts the string key into a cryptographic SecretKey object.
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Step 1 & 3: Creation of the JWT "Dish"
     * Concatenates Header/Payload and signs it with the Salt.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername()) // Step 1: Payload Subject (Fixed casing)
                .claim("UserId", user.getId().toString()) // Step 1: Custom Claim
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3000 * 1000)) // 50-minute expiry
                .signWith(getSecretKey()) // Step 3: Hashing with Salt
                .compact(); // Final "Envelope" creation
    }

    /**
     * Verification Logic: Checking the "Dish"
     * Extracts the username after verifying the signature.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey()) // Re-calculates signature using Salt
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.get("UserId", String.class));
    }
    
}
