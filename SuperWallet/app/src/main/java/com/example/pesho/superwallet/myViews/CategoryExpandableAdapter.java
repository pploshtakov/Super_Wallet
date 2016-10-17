package com.example.pesho.superwallet.myViews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.example.pesho.superwallet.AddTransactionsActivity;
import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;

import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.Locale;

public class CategoryExpandableAdapter extends ExpandableRecyclerAdapter<TransactionsListCategory, TransactionsListTransaction, TransactionsParentViewHolder, TransactionsChildViewHolder> {

    LayoutInflater mInflater;
    Context context;

    public CategoryExpandableAdapter(Context context, List<TransactionsListCategory> parentItemList) {
        super(parentItemList);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TransactionsParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_transactions_parent, parentViewGroup, false);
        view.setBackgroundColor(ContextCompat.getColor(context,R.color.md_green_100));
        return new TransactionsParentViewHolder(view);
    }

    @NonNull
    @Override
    public TransactionsChildViewHolder onCreateChildViewHolder(@NonNull final ViewGroup childViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_transaction_child, childViewGroup, false);
        return new TransactionsChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(@NonNull TransactionsParentViewHolder parentViewHolder, int parentPosition, @NonNull TransactionsListCategory parent) {
        TransactionsListCategory category = parent;
        parentViewHolder.mCategoryTitleTextView.setText(category.getTitle());
    }

    @Override
    public void onBindChildViewHolder(@NonNull TransactionsChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull TransactionsListTransaction child) {
        final TransactionsListTransaction transactionChild = child;
        childViewHolder.mTransactionDateText.setText(buildDateText(transactionChild.getDate()));
        childViewHolder.getmTransactionAmountText.setText(String.valueOf(transactionChild.getTransaction().getAmount()) + " BGN");


        if (transactionChild.getTransaction().getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Income)) {
            childViewHolder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_600));
        } else if (transactionChild.getTransaction().getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Expense)) {
            childViewHolder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_600));
        } else {
            childViewHolder.root.setBackgroundColor(ContextCompat.getColor(context,R.color.black));
        }
        childViewHolder.mTransactionDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTransactionsActivity.class);
                intent.putExtra("showInfoFor", transactionChild.getTransaction().getTransactionId());
                context.startActivity(intent);
            }
        });
    }

    StringBuilder dateBuilder = new StringBuilder();
    public String buildDateText (LocalDateTime dateTime) {
        dateBuilder.setLength(0);
        dateBuilder.append(dateTime.getDayOfMonth());
        dateBuilder.append(' ');
        dateBuilder.append(dateTime.monthOfYear().getAsText(new Locale("bg")).toUpperCase());
        dateBuilder.append(' ');
        dateBuilder.append(dateTime.getYear());
        dateBuilder.append(' ');
        dateBuilder.append(dateTime.getHourOfDay());
        dateBuilder.append(':');
        dateBuilder.append(dateTime.getMinuteOfHour());
        return dateBuilder.toString();
    }
}
