package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.AccountRepository;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    // METODO AUXILIAR - USUARIO AUTENTICADO

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado nao encontrado"));
    }

    // METODO AUXILIAR - VALIDAR DONO DA CONTA

    private void validateOwnership(Account account) {

        User authenticatedUser = getAuthenticatedUser();

        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Acesso negado");
        }
    }

    // METODO: CRIAR CONTA

    public AccountResponseDTO createAccount(AccountRequestDTO dto) {

        User authenticatedUser = getAuthenticatedUser();

        Account account = new Account();
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        account.setUser(authenticatedUser);

        Account savedAccount = accountRepository.save(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(savedAccount.getId());
        response.setName(savedAccount.getName());
        response.setBalance(savedAccount.getBalance());

        return response;
    }


    // METODO: BUSCAR CONTA POR ID

    public AccountResponseDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conta nao encontrada"));

        validateOwnership(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(account.getId());
        response.setName(account.getName());
        response.setBalance(account.getBalance());

        return response;
    }


    // METODO: LISTAR CONTAS DO USUARIO LOGADO

    public List<AccountResponseDTO> getAllAccounts() {

        User authenticatedUser = getAuthenticatedUser();

        List<Account> accounts =
                accountRepository.findByUserId(authenticatedUser.getId());

        return accounts.stream().map(account -> {
            AccountResponseDTO dto = new AccountResponseDTO();
            dto.setId(account.getId());
            dto.setName(account.getName());
            dto.setBalance(account.getBalance());
            return dto;
        }).toList();
    }


    // METODO: ATUALIZAR CONTA

    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO dto) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conta nao encontrada"));

        validateOwnership(account);

        account.setName(dto.getName());
        account.setBalance(dto.getBalance());

        Account updatedAccount = accountRepository.save(account);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(updatedAccount.getId());
        response.setName(updatedAccount.getName());
        response.setBalance(updatedAccount.getBalance());

        return response;
    }


    // METODO: DELETAR CONTA

    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conta nao encontrada"));

        validateOwnership(account);

        accountRepository.delete(account);
    }
}