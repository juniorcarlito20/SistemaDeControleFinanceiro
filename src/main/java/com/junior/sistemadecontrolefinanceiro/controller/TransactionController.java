package com.junior.sistemadecontrolefinanceiro.controller;

import com.junior.sistemadecontrolefinanceiro.dto.TransactionRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.TransactionResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.User; // Importar sua entidade User
import com.junior.sistemadecontrolefinanceiro.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importar a anotação
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal User userLogado) { // Injeta o usuário logado

        // Passa o ID do usuário para validar se a conta destino é realmente dele
        return ResponseEntity.ok(transactionService.createTransaction(dto, userLogado.getId()));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll(
            @AuthenticationPrincipal User userLogado) { // Injeta o usuário logado

        // Passa o ID do usuário para listar apenas as transações das contas dele
        return ResponseEntity.ok(transactionService.getAllTransactions(userLogado.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) { // Injeta o usuário logado

        // Passa o ID do usuário para verificar se a transação buscada pertence a ele
        return ResponseEntity.ok(transactionService.getTransactionById(id, userLogado.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TransactionRequestDTO dto,
            @AuthenticationPrincipal User userLogado) { // Injeta o usuário logado

        // Passa o ID do usuário para garantir o isolamento na alteração
        return ResponseEntity.ok(transactionService.updateTransaction(id, dto, userLogado.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User userLogado) { // Injeta o usuário logado

        // Passa o ID do usuário para garantir que ele não delete transações de outros
        transactionService.deleteTransaction(id, userLogado.getId());
        return ResponseEntity.noContent().build();
    }
}