package com.junior.sistemadecontrolefinanceiro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 💡 CORREÇÃO: Ignora a validação de Token para rotas de autenticação e documentação
        if (requestURI.contains("/auth/") || requestURI.contains("/swagger-ui") || requestURI.contains("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtém o cabeçalho Authorization
        final String authHeader = request.getHeader("Authorization");

        // Verifica se existe o token e se ele começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove o prefixo "Bearer " para isolar apenas a string do token
        String token = authHeader.substring(7);

        // Extrai o email armazenado dentro do token
        String email = jwtService.extractUsername(token);

        // Verifica se existe o email e se o usuário ainda não está autenticado no contexto atual
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Busca o usuário no banco de dados através do e-mail
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Valida se o token pertence àquele usuário e se não está expirado
            if (jwtService.isTokenValid(token, userDetails)) {

                // Cria o objeto de autenticação com as permissões (Authorities) do usuário
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Adiciona os detalhes da requisição HTTP à autenticação
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Aplica o usuário autenticado dentro do contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("AUTENTICADO: " + userDetails.getUsername());
                System.out.println("AUTHORITIES: " + userDetails.getAuthorities());
            }
        }

        // Continua o fluxo normal da requisição
        filterChain.doFilter(request, response);
    }
}