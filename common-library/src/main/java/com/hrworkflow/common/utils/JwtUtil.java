package com.hrworkflow.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String signingKey = "Zw1dsOA1hqSKlX9PVREovrt2S0h8AydmIvb4FwC/Ij4=";

    private Long expiration = 86400000L;

    public String generateToken(Map<String, Object> extraClaims, String username) {

        return Jwts.builder()
                .subject(username)
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Long extractUserId(String token) {

        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey() {

        byte[] encodedKey = Base64.getEncoder().encode(signingKey.getBytes());
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
