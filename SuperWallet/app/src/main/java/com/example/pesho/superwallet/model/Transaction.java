package com.example.pesho.superwallet.model;

import android.app.Activity;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Pesho on 9/29/2016.
 */
public class Transaction {
    public enum TRANSACTIONS_TYPE {Income, Expense,Transfer}
	private int transactionId;
    private LocalDateTime date;
    private String description;
    private TRANSACTIONS_TYPE transactionType;
    private Category category;
    private double amount;

    public Transaction(int transactionId, LocalDateTime date, TRANSACTIONS_TYPE transactionType, double amount) {
		this.transactionId = transactionId;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
    }

	public int getTransactionId() {
		return transactionId;
	}

    public LocalDateTime getDate() {
        return date;
    }

	public Timestamp getDateAsSQLTimestamp() {
		return new Timestamp(getDate().toDateTime().getMillis());
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public String getDescription() {
        return description;
    }

    public TRANSACTIONS_TYPE getTransactionType() {
        return transactionType;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

	public void setCategory(Category category) {
		this.category = category;
	}

	public static LocalDateTime getDateFromSQLTimestamp(String timestamp_str) {
		Timestamp timestamp = new Timestamp(Long.parseLong(timestamp_str));
		return LocalDateTime.fromDateFields(timestamp);
	}
}
