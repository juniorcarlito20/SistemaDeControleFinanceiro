package com.junior.sistemadecontrolefinanceiro.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String role;

    // --- MÉTODOS OBRIGATÓRIOS DO USERDETAILS ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Como o foco é apenas bloqueio simples, passamos uma role padrão baseada no seu campo
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return this.email; // O Spring usa o "username" para a credencial de login (no seu caso, o email)
    }

    @Override
    public String getPassword() {
        return this.password; // Retorna a senha criptografada
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta não expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta não bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais não expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // Usuário ativo
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}