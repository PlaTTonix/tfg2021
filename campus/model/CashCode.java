package com.tfg.campus.model;

public class CashCode {
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public CashCode() {
    }

    public CashCode(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CashCode{" +
                "amount=" + amount +
                '}';
    }
}
