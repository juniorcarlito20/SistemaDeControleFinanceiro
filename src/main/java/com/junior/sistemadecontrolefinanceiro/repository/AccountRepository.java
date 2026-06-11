package com.junior.sistemadecontrolefinanceiro.repository;

import com.junior.sistemadecontrolefinanceiro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Busca uma conta específica apenas se ela pertencer ao usuário logado
    Optional<Account> findByIdAndUserId(Long id, Long userId);

    // Lista todas as contas que pertencem APENAS ao usuário logado
    List<Account> findByUserId(Long userId);
}