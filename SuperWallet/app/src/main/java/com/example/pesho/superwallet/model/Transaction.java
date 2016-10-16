package com.example.pesho.superwallet.model;

import android.app.Activity;
import android.location.Location;

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
    protected Account accountFrom;
    private TRANSACTIONS_TYPE transactionType;
    private Category category;
    private double amount;
    private Location location;

    public Transaction(int transactionId, LocalDateTime date, TRANSACTIONS_TYPE transactionType, double amount, Account accountFrom) {
		this.transactionId = transactionId;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountFrom = accountFrom;
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
		Timestamp timestamp = Timestamp.valueOf(timestamp_str);
		return LocalDateTime.fromDateFields(timestamp);
	}

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
