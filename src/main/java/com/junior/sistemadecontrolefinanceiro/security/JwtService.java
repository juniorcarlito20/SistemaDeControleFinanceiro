package com.junior.sistemadecontrolefinanceiro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Sua chave secreta (Certifique-se de mantê-la longa no seu ambiente)
    private static final String SECRET_KEY =
            "minha-chave-secreta-super-segura-com-mais-de-32-caracteres";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Gerar token para um email específico (Expira em 1 hora)
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(key)
                .compact();
    }

    // Ler token e extrair o email (username)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // AJUSTE: Validar token comparando com o UserDetails e checando expiração
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Verifica se o email do token bate com o do banco E se o token não expirou
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Método auxiliar para checar se o token já passou da data de expiração
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Método auxiliar para extrair todas as Claims (Payload) do token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}