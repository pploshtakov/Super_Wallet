package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Category {
    private int categoryId;
    private Transaction.TRANSACTIONS_TYPE transactionType;
    private String categoryName;
    private int categoryIcon;
	private String categoryDescription;

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

	public String getCategoryDescription() { return categoryDescription;	}

	public Transaction.TRANSACTIONS_TYPE getTransactionType() { return transactionType; }

	public void setCategoryDescription(String description) { categoryDescription = description; }
	public void setCategoryName(String name) { categoryName = name; }
	public void setCategoryIcon(int icon) { categoryIcon = icon; }
	public void setCategoryType(Transaction.TRANSACTIONS_TYPE type) { transactionType = type;	}
}
