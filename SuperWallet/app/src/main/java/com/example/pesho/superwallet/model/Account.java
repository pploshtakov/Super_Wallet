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

    public Account(int accountId, String accountName, double balance, ACCOUNT_TYPE accountType) {
		this.accountId = accountId;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
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

	@Override
    public String toString() {
        return accountName + "/" + String.valueOf(balance) ;
    }
}
