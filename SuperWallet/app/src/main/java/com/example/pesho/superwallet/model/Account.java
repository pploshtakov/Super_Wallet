package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Account {
    public enum ACCOUNT_TYPE {CASH, ACCOUNT, CARD}
	private int accountId;
    private String accountName;
    private double balance;
    public ACCOUNT_TYPE accountType;
	private String accountDescription;

    public Account(int accountId, String accountName, double balance, ACCOUNT_TYPE accountType) {
		this.accountId = accountId;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
		accountDescription = "";
    }

	public String getAccountName() {
		return accountName;
	}

	public int getAccountId() {
		return accountId;
	}

	public double getAccountBalance() {
		return balance;
	}

	public ACCOUNT_TYPE getAccountType() {
		return accountType;
	}

	public void setAccountDescription(String accountDescription) {
		this.accountDescription = accountDescription;
	}

	public String getAccountDescription() {
		return accountDescription;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountType(ACCOUNT_TYPE accountType) {
		this.accountType = accountType;
	}

	@Override
    public String toString() {
        return accountName + "/" + String.valueOf(balance) ;
    }

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
