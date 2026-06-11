package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User; // Importar sua entidade User
import com.junior.sistemadecontrolefinanceiro.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importar a anotação do Spring
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(
            @RequestBody AccountRequestDTO dto,
            @AuthenticationPrincipal User userLogado) { // Pega o usuário logado

        // Passa o ID do usuário para vincular a conta a ele na criação
        return ResponseEntity.ok(accountService.createAccount(dto, userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) { // Pega o usuário logado

        // Passa o ID da conta E o ID do usuário logado para validar o acesso
        return ResponseEntity.ok(accountService.getAccountById(id, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(
            @AuthenticationPrincipal User userLogado) { // Pega o usuário logado

        // Listará apenas as contas pertencentes ao usuário logado
        return ResponseEntity.ok(accountService.getAllAccounts(userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO dto,
            @AuthenticationPrincipal User userLogado) { // Pega o usuário logado

        // Garante que só atualiza se a conta for do usuário logado
        return ResponseEntity.ok(accountService.updateAccount(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) { // Pega o usuário logado

        // Garante que só deleta se a conta for do usuário logado
        accountService.deleteAccount(id, userLogado.getId());
        return ResponseEntity.noContent().build();
    }
}