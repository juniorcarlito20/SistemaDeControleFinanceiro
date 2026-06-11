package com.junior.sistemadecontrolefinanceiro.repository;

import com.junior.sistemadecontrolefinanceiro.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Busca uma transação específica garantindo que ela pertença a uma conta do usuário logado
    Optional<Transaction> findByIdAndAccountUserId(Long id, Long userId);

    // Lista todas as transações de todas as contas que pertencem ao usuário logado
    List<Transaction> findByAccountUserId(Long userId);
}