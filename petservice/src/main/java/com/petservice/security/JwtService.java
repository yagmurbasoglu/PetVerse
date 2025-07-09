package com.petservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtDecoder jwtDecoder;

    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
