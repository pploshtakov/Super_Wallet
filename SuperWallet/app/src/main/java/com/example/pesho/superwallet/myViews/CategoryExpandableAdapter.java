package com.example.pesho.superwallet.myViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;

import java.util.List;

public class CategoryExpandableAdapter extends ExpandableRecyclerAdapter<TransactionsListCategory, TransactionsListTransaction, TransactionsParentViewHolder, TransactionsChildViewHolder> {

    LayoutInflater mInflater;
    Context contect;

    public CategoryExpandableAdapter(Context context, List<TransactionsListCategory> parentItemList) {
        super(parentItemList);

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TransactionsParentViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_transactions_parent, parentViewGroup, false);
        return new TransactionsParentViewHolder(view);
    }

    @NonNull
    @Override
    public TransactionsChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
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
        TransactionsListTransaction transaction = child;
        childViewHolder.mTransactionDateText.setText(transaction.getDate().toString());
    }
}
