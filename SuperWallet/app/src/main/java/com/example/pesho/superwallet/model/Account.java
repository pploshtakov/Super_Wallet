package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Account {
    public enum ACCOUNT_TYPE {CASH, ACCOUNT, CARD};
    private String accountName;
    private double balance;
    public ACCOUNT_TYPE accountType;

    public Account(String accountName, double balance, ACCOUNT_TYPE accountType) {
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
    }
}
