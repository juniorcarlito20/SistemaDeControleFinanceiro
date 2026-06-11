package com.junior.sistemadecontrolefinanceiro.repository;

import com.junior.sistemadecontrolefinanceiro.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Busca a categoria apenas se ela pertencer ao usuário logado
    Optional<Category> findByIdAndUserId(Long id, Long userId);

    // Lista as categorias exclusivas do usuário logado
    List<Category> findByUserId(Long userId);
}