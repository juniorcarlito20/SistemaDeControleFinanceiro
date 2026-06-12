package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import com.junior.sistemadecontrolefinanceiro.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    public AccountController(
            AccountService accountService,
            UserRepository userRepository) {

        this.accountService = accountService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(
            @RequestBody AccountRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                accountService.createAccount(dto, userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                accountService.getAccountById(id, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                accountService.getAllAccounts(userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                accountService.updateAccount(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        accountService.deleteAccount(id, userLogado.getId());

        return ResponseEntity.noContent().build();
    }
}