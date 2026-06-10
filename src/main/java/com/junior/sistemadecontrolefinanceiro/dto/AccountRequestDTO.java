package com.junior.sistemadecontrolefinanceiro.dto;

import java.math.BigDecimal;

public class AccountRequestDTO {
    private String name;
    private BigDecimal balance;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
