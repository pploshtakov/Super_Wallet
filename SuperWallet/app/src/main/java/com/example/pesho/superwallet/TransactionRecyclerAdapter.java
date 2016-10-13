package com.example.pesho.superwallet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pesho.superwallet.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Pesho on 10/13/2016.
 */

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.MyViewHolder> {

    private ArrayList<Transaction> transactions;
    private RecyclerView myRecyclerView;

    TransactionRecyclerAdapter (ArrayList<Transaction> transactions, RecyclerView myRecyclerView) {
        this.myRecyclerView = myRecyclerView;
        if (transactions != null) {
            this.transactions = transactions;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_info_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(myView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.categoryTV.setText(transaction.getCategory().getCategoryName());
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        holder.dateTV.setText(sdf.format(transaction.getDate().toDate()));
        holder.amountTV.setText(String.valueOf(transaction.getAmount()));

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTV;
        TextView dateTV;
        TextView amountTV;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryTV = (TextView) itemView.findViewById(R.id.transaction_card_category);
            dateTV = (TextView) itemView.findViewById(R.id.transaction_card_date);
            amountTV = (TextView) itemView.findViewById(R.id.transaction_card_amount);
        }
    }

    public void setNewList(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
