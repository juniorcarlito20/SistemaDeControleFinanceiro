package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.TransactionRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.TransactionResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import com.junior.sistemadecontrolefinanceiro.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionController(
            TransactionService transactionService,
            UserRepository userRepository) {

        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                transactionService.createTransaction(dto, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                transactionService.getAllTransactions(userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                transactionService.getTransactionById(id, userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        return ResponseEntity.ok(
                transactionService.updateTransaction(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User userLogado = getAuthenticatedUser(userDetails);

        transactionService.deleteTransaction(id, userLogado.getId());

        return ResponseEntity.noContent().build();
    }
}