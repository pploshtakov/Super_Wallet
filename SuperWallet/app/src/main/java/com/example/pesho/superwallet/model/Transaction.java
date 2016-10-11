package com.example.pesho.superwallet.model;

import android.app.Activity;

import java.util.Date;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Transaction {
    public enum TRANSACTIONS_TYPE {Income, Expense,Transfer};
    private Date date;
    private String description;
    private TRANSACTIONS_TYPE transactionType;
    private Category category;
    private double amount;

    public Transaction(Date date, String description, TRANSACTIONS_TYPE transactionType, double amount, Category category) {
        this.date = date;
        this.description = description;
        this.transactionType = transactionType;
        this.amount = amount;
        this.category = category;
        //DBManager.getInstance(new Activity()).addTransaction(date.toString(), description, transactionType, amount, category);
    }

    public Date getDate() {
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
