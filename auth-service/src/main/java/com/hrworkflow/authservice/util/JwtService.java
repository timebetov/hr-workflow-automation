package com.hrworkflow.authservice.util;

import com.hrworkflow.authservice.dto.UserResponseDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.jwt.secret-key}")
    private String jwtSigningKey;

    public String generateJwtToken(UserResponseDTO userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("id", userDetails.getUserId());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        return generateToken(claims, userDetails);
    }


    private String generateToken(Map<String, Object> extraClaims, UserResponseDTO userDetails) {

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey())
                .compact();
    }

    public Key getSigningKey() {

        byte[] encodedKey = Base64.getEncoder().encode(jwtSigningKey.getBytes());
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
