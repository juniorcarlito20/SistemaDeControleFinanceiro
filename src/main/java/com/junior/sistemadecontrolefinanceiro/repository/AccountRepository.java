package com.junior.sistemadecontrolefinanceiro.repository;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface  AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

}
