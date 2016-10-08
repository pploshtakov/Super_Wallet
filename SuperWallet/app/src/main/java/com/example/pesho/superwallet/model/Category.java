package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Category {
    private int categoryId;
    private Transaction.TRANSACTIONS_TYPE transactionType;
    private String categoryName;
    private int categoryIcon;

    public Category(int categoryId, Transaction.TRANSACTIONS_TYPE transactionType, String categoryName, int categoryIcon) {
        this.categoryId = categoryId;
        this.transactionType = transactionType;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

	public int getCategoryId() { return categoryId; }

	public int getCategoryIcon() { return categoryIcon; }
}
