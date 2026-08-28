package com.datalyze.alquileres.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.issuer}")
    private String issuer;
    private Algorithm algorithm;

    @PostConstruct
    protected void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(String username, String rol, List<Integer> sedesIds) {
        return JWT.create()
                .withSubject(username)
                .withIssuer(issuer)
                .withClaim("rol", rol)
                .withClaim("sedes", sedesIds) // Inyección del contexto de aislamiento
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 12 horas
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token) {
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }
}
