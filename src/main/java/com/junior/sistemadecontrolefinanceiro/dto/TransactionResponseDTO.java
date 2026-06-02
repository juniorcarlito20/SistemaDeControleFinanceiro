package com.junior.sistemadecontrolefinanceiro.dto;

import java.math.BigDecimal;

public class TransactionResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private Long accountId;
    private Long categoryId;

    public TransactionResponseDTO(
            Long id,
            String description,
            BigDecimal amount,
            Long accountId,
            Long categoryId) {

        this.id = id;
        this.description = description;
        this.amount = amount;
        this.accountId = accountId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
