package com.PetVerse.apigateway.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtUtil {

    private final PublicKey publicKey;

    public JwtUtil() {
        try {
            String key = new String(this.getClass().getResourceAsStream("/jwt/app.pub").readAllBytes());
            key = key.replace("-----BEGIN PUBLIC KEY-----", "")
                     .replace("-----END PUBLIC KEY-----", "")
                     .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            this.publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key for JWT verification", e);
        }
    }

    public Claims validateToken(String token) throws JwtException {
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
