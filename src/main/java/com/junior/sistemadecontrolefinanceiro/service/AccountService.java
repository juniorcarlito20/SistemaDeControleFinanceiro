package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.repository.AccountRepository;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // Metodo para criar conta
    public AccountResponseDTO createAccount(AccountRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        Account account = new Account();
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(savedAccount.getId());
        response.setName(savedAccount.getName());
        response.setBalance(savedAccount.getBalance());

        return response;
    }

    // Metodo para buscar conta por id
    public AccountResponseDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada"));

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(account.getId());
        response.setName(account.getName());
        response.setBalance(account.getBalance());

        return response;
    }

    // Metodo para listar todas as contas
    public List<AccountResponseDTO> getAllAccounts() {

        List<Account> accounts = accountRepository.findAll();

        return accounts.stream().map(account -> {
            AccountResponseDTO dto = new AccountResponseDTO();
            dto.setId(account.getId());
            dto.setName(account.getName());
            dto.setBalance(account.getBalance());
            return dto;
        }).toList();
    }

    // Metodo para atualizar conta
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO dto) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada"));

        account.setName(dto.getName());
        account.setBalance(dto.getBalance());

        Account updatedAccount = accountRepository.save(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(updatedAccount.getId());
        response.setName(updatedAccount.getName());
        response.setBalance(updatedAccount.getBalance());

        return response;
    }

    // Metodo para deletar conta
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada"));

        accountRepository.delete(account);
    }
}