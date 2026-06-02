package com.junior.sistemadecontrolefinanceiro.repository;

import com.junior.sistemadecontrolefinanceiro.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
}