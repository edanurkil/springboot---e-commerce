package com.example.e_commerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception){
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getExpiresAt();
        } catch (JWTVerificationException exception){
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = extractExpiration(token);
        return expirationDate == null || expirationDate.before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}