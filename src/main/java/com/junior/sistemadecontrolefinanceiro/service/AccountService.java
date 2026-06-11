package com.junior.sistemadecontrolefinanceiro.service;

import com.junior.sistemadecontrolefinanceiro.dto.AccountRequestDTO;
import com.junior.sistemadecontrolefinanceiro.dto.AccountResponseDTO;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import com.junior.sistemadecontrolefinanceiro.entity.User;
import com.junior.sistemadecontrolefinanceiro.exceptions.ResourceNotFoundException;
import com.junior.sistemadecontrolefinanceiro.repository.AccountRepository;
import com.junior.sistemadecontrolefinanceiro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // METODO: CRIAR CONTA (Agora recebendo o userId do Controller)
    public AccountResponseDTO createAccount(AccountRequestDTO dto, Long userId) {
        // Buscamos o usuário apenas para mapear a referência no banco
        User authenticatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Account account = new Account();
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        account.setUser(authenticatedUser);

        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }

    // METODO: BUSCAR CONTA POR ID E USER ID
    public AccountResponseDTO getAccountById(Long id, Long userId) {
        // Tentamos buscar a conta amarrada ao ID do usuário logado.
        // Se o Usuário 2 tentar passar o ID do Usuário 1, vai cair direto na Exception de "não encontrada/acesso negado"
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou acesso negado"));

        return convertToDTO(account);
    }

    // METODO: LISTAR CONTAS DO USUÁRIO LOGADO
    public List<AccountResponseDTO> getAllAccounts(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // METODO: ATUALIZAR CONTA
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO dto, Long userId) {
        // Só permite capturar a conta para atualização se ela de fato pertencer ao usuário logado
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou acesso negado"));

        account.setName(dto.getName());
        account.setBalance(dto.getBalance());

        Account updatedAccount = accountRepository.save(account);
        return convertToDTO(updatedAccount);
    }

    // METODO: DELETAR CONTA
    public void deleteAccount(Long id, Long userId) {
        // Só permite deletar se a conta pertencer ao usuário logado
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou acesso negado"));

        accountRepository.delete(account);
    }

    // Metodo auxiliar isolado para evitar repetição de código (Clean Code)
    private AccountResponseDTO convertToDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setBalance(account.getBalance());
        return dto;
    }
}