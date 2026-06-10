package com.junior.sistemadecontrolefinanceiro.dto;

import com.junior.sistemadecontrolefinanceiro.enums.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransactionRequestDTO {

    private String description;

    @NotNull(message = "Valor é obrigatório")
    private BigDecimal amount;

    @NotNull(message = "Tipo da transação é obrigatório")
    private TransactionType type;

    private Long accountId;

    private Long categoryId;

    public TransactionRequestDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}