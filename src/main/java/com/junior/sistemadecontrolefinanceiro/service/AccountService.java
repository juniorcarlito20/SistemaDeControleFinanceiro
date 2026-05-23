package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.repository.AccountRepository;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // metodo para criar conta
    public AccountResponseDTO createAccount (AccountRequestDTO dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        Account account= new Account();
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
}
