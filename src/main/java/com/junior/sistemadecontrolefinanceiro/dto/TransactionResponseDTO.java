package com.junior.sistemadecontrolefinanceiro.dto;

import com.junior.sistemadecontrolefinanceiro.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private Long accountId;
    private Long categoryId;

    public TransactionResponseDTO(
            Long id,
            String description,
            BigDecimal amount,
            TransactionType type,
            Long accountId,
            Long categoryId) {

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}