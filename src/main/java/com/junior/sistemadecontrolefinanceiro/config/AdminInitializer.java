package com.junior.sistemadecontrolefinanceiro.config;

import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeta a senha dinamicamente a partir do application.properties / Variável de Ambiente
    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // METODO PRINCIPAL: EXECUÇÃO NA INICIALIZAÇÃO DO SISTEMA

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@sistema.com";

        // SEGURANÇA: Só cria o administrador se ele ainda não existir no banco
        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            User admin = new User();
            admin.setName("Administrador Global");
            admin.setEmail(adminEmail);

            // Criptografa a senha injetada pela variável de ambiente de forma segura
            admin.setPassword(passwordEncoder.encode(adminPassword));

            // Define o perfil como ADMIN puro
            admin.setRole("ADMIN");

            userRepository.save(admin);

            System.out.println(" [SISTEMA] Usuário inicial com perfil ADMIN criado com segurança via Variável de Ambiente!");
        } else {
            System.out.println("[SISTEMA] Administrador já existente. Ignorando criação.");
        }
    }
}