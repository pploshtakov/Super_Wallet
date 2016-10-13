package com.example.pesho.superwallet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsListFragment extends Fragment {

	LocalDateTime startDate;
	LocalDateTime endDate;
	ArrayList<Transaction> transactions;

    public TransactionsListFragment() {
        // Required empty public constructor
		startDate = new LocalDateTime();
		startDate = startDate.withTime(0, 0, 0, 0);
		endDate = new LocalDateTime();
		endDate = endDate.plusDays(1).withTime(0,0,0,0);
		Log.e("SuperWallet ", "Fragment from " + startDate + " to " + endDate);
		ArrayList<Transaction> transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
		this.transactions = transactions;
		for (Transaction t: transactions) {
			Log.e("SuperWallet ", "Transaction id: " + t.getTransactionId() + " for DateTime: " + t.getDate());
		}
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_transactions_list, container, false);
		transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
        return root;
    }

}
