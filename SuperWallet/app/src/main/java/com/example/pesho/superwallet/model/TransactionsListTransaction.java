package com.example.pesho.superwallet.model;

import org.joda.time.LocalDateTime;

public class TransactionsListTransaction {

    private LocalDateTime mDate;
    private Transaction transaction;

    public TransactionsListTransaction(LocalDateTime date, Transaction transaction) {
        mDate = date;
        this.transaction = transaction;
    }

    public LocalDateTime getDate() {
        return mDate;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
