package com.junior.sistemadecontrolefinanceiro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "minha-chave-secreta-super-segura-com-mais-de-32-caracteres";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //Gerar token para um email específico
    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    //Ler token e extrair o email (username)
    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    //Validar token comparando o email do token com o email fornecido(verdadeiro ou falso)
    public boolean isTokenValid(String token, String email) {

        String username = extractUsername(token);

        return username.equals(email);
    }
}