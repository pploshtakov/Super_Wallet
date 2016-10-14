package com.example.pesho.superwallet.model;

import org.joda.time.LocalDateTime;

/**
 * Created by EpsiloN on 10/12/2016.
 */

public class Transfer extends Transaction {


	private Account accountTo;

	public Transfer(int transactionId, LocalDateTime date, double amount, Account accountFrom, Account accountTo) {
		super(transactionId, date, TRANSACTIONS_TYPE.Transfer, amount, accountFrom);
		this.accountTo = accountTo;
	}



	public Account getAccountTo() {
		return accountTo;
	}
}
