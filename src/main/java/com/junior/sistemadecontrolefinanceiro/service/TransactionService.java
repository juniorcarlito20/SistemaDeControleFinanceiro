package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.TransactionRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.TransactionResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import com.junior.sistemadecontrolefinanceiro.entity.Category;
import com.junior.sistemadecontrolefinanceiro.entity.Transaction;
import com.junior.sistemadecontrolefinanceiro.enums.TransactionType;
import com.junior.sistemadecontrolefinanceiro.exceptions.InsufficientBalanceException;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.AccountRepository;
import com.junior.sistemadecontrolefinanceiro.repository.CategoryRepository;
import com.junior.sistemadecontrolefinanceiro.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    // Metodo para criar transacao e atualizar saldo da conta
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conta não encontrada com id: " + dto.getAccountId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada com id: " + dto.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setAccount(account);
        transaction.setCategory(category);

        if (dto.getType() == TransactionType.INCOME) {

            account.setBalance(
                    account.getBalance().add(dto.getAmount())
            );

        } else {

            if (account.getBalance().compareTo(dto.getAmount()) < 0) {
                throw new InsufficientBalanceException("Saldo insuficiente");
            }

            account.setBalance(
                    account.getBalance().subtract(dto.getAmount())
            );
        }

        accountRepository.save(account);

        Transaction saved = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                saved.getId(),
                saved.getDescription(),
                saved.getAmount(),
                saved.getType(),
                saved.getAccount().getId(),
                saved.getCategory().getId()
        );
    }

    // Metodo para listar todas as transacoes
    public List<TransactionResponseDTO> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getDescription(),
                        t.getAmount(),
                        t.getType(),
                        t.getAccount().getId(),
                        t.getCategory().getId()
                ))
                .collect(Collectors.toList());
    }

    // Metodo para buscar transacao por id
    public TransactionResponseDTO getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada com id: " + id));

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getAccount().getId(),
                transaction.getCategory().getId()
        );
    }

    // Metodo para atualizar transacao
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO dto) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada com id: " + id));

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conta não encontrada com id: " + dto.getAccountId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada com id: " + dto.getCategoryId()));

        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                updated.getId(),
                updated.getDescription(),
                updated.getAmount(),
                updated.getType(),
                updated.getAccount().getId(),
                updated.getCategory().getId()
        );
    }

    // Metodo para deletar transacao
    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada com id: " + id));

        transactionRepository.delete(transaction);
    }
}