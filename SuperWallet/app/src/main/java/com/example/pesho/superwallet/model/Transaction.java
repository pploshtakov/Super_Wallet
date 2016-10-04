package com.example.pesho.superwallet.model;

import java.util.Date;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Transaction {
    public enum TRANSACTIONS_TYPE {Income, Expense,Transfer};
    private String date;
    private String description;
    private TRANSACTIONS_TYPE transactionType;
    private Category category;
    private double amount;

    public Transaction(String date, String description, TRANSACTIONS_TYPE transactionType, double amount, Category category) {
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public TRANSACTIONS_TYPE getTransactionType() {
        return transactionType;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }


}
