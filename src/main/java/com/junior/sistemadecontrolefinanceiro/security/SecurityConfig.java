package com.junior.sistemadecontrolefinanceiro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Injeção do filtro JWT via construtor
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilitado pois usamos JWT (Stateless)

                .cors(Customizer.withDefaults()) // Habilita CORS com configuração padrão (pode ser personalizada se necessário)

                // Define a política de sessão como STATELESS (não cria cookies de sessão)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define as regras de permissão das rotas
                .authorizeHttpRequests(auth -> auth
                        // Rotas totalmente públicas (Swagger e endpoints de autenticação)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/auth/login",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Apenas o cadastro de usuários (POST) é público. GET/PUT/DELETE ficam protegidos.
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        // Qualquer outra rota do sistema financeiro exige autenticação
                        .anyRequest().authenticated()
                )

                // Adiciona o filtro JWT antes do filtro de autenticação padrão do Spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}