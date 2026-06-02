package com.junior.sistemadecontrolefinanceiro.repository;
import com.junior.sistemadecontrolefinanceiro.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
