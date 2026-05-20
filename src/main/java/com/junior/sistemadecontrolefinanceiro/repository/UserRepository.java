package com.junior.sistemadecontrolefinanceiro.repository;

import com.junior.sistemadecontrolefinanceiro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long>{
}
