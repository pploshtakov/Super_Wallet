package com.example.pesho.superwallet.model;

import org.joda.time.LocalDateTime;

public class TransactionsListTransaction {

    private LocalDateTime mDate;

    public TransactionsListTransaction(LocalDateTime date) {
        mDate = date;
    }

    public LocalDateTime getDate() {
        return mDate;
    }
}
