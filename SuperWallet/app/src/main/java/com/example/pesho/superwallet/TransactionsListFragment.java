package com.example.pesho.superwallet;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsListFragment extends Fragment {

	LocalDateTime startDate;
	LocalDateTime endDate;
	ArrayList<Transaction> transactions;

	TransactionRecyclerAdapter adapter;
	RecyclerView myRecyclerView;

	TextView periodTV;
	TextView incomeTV;
	TextView expenseTV;
	TextView totalTV;


    public TransactionsListFragment() {
        // Required empty public constructor
		startDate = new LocalDateTime();
		startDate = startDate.minusYears(10);
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
		incomeTV = (TextView) root.findViewById(R.id.main_income_tv);
		expenseTV = (TextView) root.findViewById(R.id.main_expence_tv);
		totalTV = (TextView) root.findViewById(R.id.main_total_tv);


		transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
		myRecyclerView = (RecyclerView) root.findViewById(R.id.transactions_recycler_view);
		myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new TransactionRecyclerAdapter(transactions, myRecyclerView);
		myRecyclerView.setAdapter(adapter);
		//set period
		periodTV = (TextView) root.findViewById(R.id.transaction_list_date);
		periodTV.setText("All transactions");
		calculateReports();
        return root;
    }

	private void calculateReports() {
		double income = 0;
		double expense = 0;
		double total = 0;
		if (transactions != null) {
			for (Transaction tr : transactions) {
				if (tr.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Income)) {
					income += tr.getAmount();
				} else if (tr.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Expense)) {
					expense += tr.getAmount();
				}
			}
		}
		total = income - expense;
		incomeTV.setText(String.valueOf(income));
		expenseTV.setText(String.valueOf(expense));
		totalTV.setText(String.valueOf(total));
		if (total < 0) {
			totalTV.setTextColor(Color.RED);
		} else {
			totalTV.setTextColor(Color.BLUE);
		}
	}

	private void setPeriod(LocalDateTime startDate, LocalDateTime endDate) {
		String myFormat = "dd.MM.yyyy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		String start = sdf.format(startDate.toDate());
		String end = sdf.format(endDate.toDate());
		periodTV.setText(start + " - " + end);
	}

	public void refreshList(LocalDateTime startDate, LocalDateTime endDate) {
		transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
		adapter = (TransactionRecyclerAdapter) myRecyclerView.getAdapter();
		adapter.setNewList(transactions);
		adapter.notifyDataSetChanged();
		calculateReports();
		setPeriod(startDate, endDate);
	}

}
