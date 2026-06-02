package com.junior.sistemadecontrolefinanceiro.repository;
import com.junior.sistemadecontrolefinanceiro.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;


public interface  AccountRepository extends JpaRepository<Account, Long> {

}
