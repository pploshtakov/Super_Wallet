package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Account {
    private String accountName;
    private double balance;
    private boolean isCash; //cash or bill

    public Account(String accountName, double balance, boolean isCash) {
        this.accountName = accountName;
        this.balance = balance;
        this.isCash = isCash;
    }
}
