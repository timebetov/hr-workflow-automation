package com.hrworkflow.identityservice.utils;

import com.hrworkflow.identityservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret-key}")
    private String signingKey;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    public String generateToken(Map<String, Object> extraClaims, String username) {

        return Jwts.builder()
                .subject(username)
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateJwtToken(User userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("id", userDetails.getId());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        return generateToken(claims, userDetails.getUsername());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSigningKey() {

        byte[] encodedKey = Base64.getEncoder().encode(signingKey.getBytes());
        return Keys.hmacShaKeyFor(encodedKey);
    }
}
