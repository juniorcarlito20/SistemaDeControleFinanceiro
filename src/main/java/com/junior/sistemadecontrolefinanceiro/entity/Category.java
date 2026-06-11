package com.junior.sistemadecontrolefinanceiro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // 💡 Dica: mudei o 'Id' para minúsculo 'id' para seguir o padrão Java CamelCase

    private String name;

    // SEGURANÇA: Vincula a categoria ao usuário que a criou
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Category() {
    }

    // Atualizado o construtor para incluir o usuário
    public Category(long id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters e Setters para o User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}