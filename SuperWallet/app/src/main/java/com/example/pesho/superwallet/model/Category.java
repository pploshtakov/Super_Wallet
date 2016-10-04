package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Category {
    private Transaction.TRANSACTIONS_TYPE transactionType;
    private String categoryName;

    public Category(Transaction.TRANSACTIONS_TYPE transactionType, String categoryName) {
        this.transactionType = transactionType;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
