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
import org.springframework.transaction.annotation.Transactional;

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

    // Metodo para criar transação e atualizar saldo da conta
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto, Long userId) {

        // SEGURANÇA: Só permite usar a conta se ela pertencer de fato ao usuário logado
        Account account = accountRepository.findByIdAndUserId(dto.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conta não encontrada ou acesso negado para o id: " + dto.getAccountId()));

        //  SEGURANÇA ATUALIZADA: Garante que a categoria usada também pertence ao usuário logado
        Category category = categoryRepository.findByIdAndUserId(dto.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada ou acesso negado com id: " + dto.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setAccount(account);
        transaction.setCategory(category);

        if (dto.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.getAmount()));
        } else {
            if (account.getBalance().compareTo(dto.getAmount()) < 0) {
                throw new InsufficientBalanceException("Saldo insuficiente");
            }
            account.setBalance(account.getBalance().subtract(dto.getAmount()));
        }

        accountRepository.save(account);
        Transaction saved = transactionRepository.save(transaction);

        return convertToDTO(saved);
    }

    // Metodo para listar todas as transações DO USUÁRIO LOGADO
    public List<TransactionResponseDTO> getAllTransactions(Long userId) {
        return transactionRepository.findByAccountUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Metodo para buscar transação por id
    public TransactionResponseDTO getTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada ou acesso negado com id: " + id));

        return convertToDTO(transaction);
    }

    // Metodo para atualizar transação
    @Transactional
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO dto, Long userId) {

        Transaction transaction = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada ou acesso negado com id: " + id));

        Account account = accountRepository.findByIdAndUserId(dto.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conta não encontrada ou acesso negado para o id: " + dto.getAccountId()));

        //  SEGURANÇA ATUALIZADA: Garante que a nova categoria opcional também pertence ao usuário logado
        Category category = categoryRepository.findByIdAndUserId(dto.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada ou acesso negado com id: " + dto.getCategoryId()));

        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setAccount(account);
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);
        return convertToDTO(updated);
    }

    // Metodo para deletar transação
    public void deleteTransaction(Long id, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndAccountUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada ou acesso negado com id: " + id));

        transactionRepository.delete(transaction);
    }

    private TransactionResponseDTO convertToDTO(Transaction t) {
        return new TransactionResponseDTO(
                t.getId(),
                t.getDescription(),
                t.getAmount(),
                t.getType(),
                t.getAccount().getId(),
                t.getCategory().getId()
        );
    }
}