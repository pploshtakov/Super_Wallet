package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Category {
    private Transaction.TransactionTypes transactionType;
    private String categoryName;

    public Category(Transaction.TransactionTypes transactionType, String categoryName) {
        this.transactionType = transactionType;
        this.categoryName = categoryName;
    }
}
