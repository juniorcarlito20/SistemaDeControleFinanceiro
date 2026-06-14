package com.junior.sistemadecontrolefinanceiro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://sistemadecontrolefinanceiro.up.railway.app")
                ))
                .info(new Info()
                        .title("Sistema de Controle Financeiro API")
                        .version("1.0")
                        .description("API para gerenciamento financeiro pessoal"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}