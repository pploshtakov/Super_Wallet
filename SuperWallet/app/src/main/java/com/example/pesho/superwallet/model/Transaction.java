package com.example.pesho.superwallet.model;

import java.util.Date;
import java.util.Objects;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Transaction {
    public enum TransactionTypes {Income, Expense,Transfer};
    private Date date;
    private String description;
    private TransactionTypes transactionType;
    private Category category;
    private double amount;

    public Transaction(Date date, String description, TransactionTypes transactionType, double amount, Category category) {
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.amount = amount;
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public TransactionTypes getTransactionType() {
        return transactionType;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }


}
