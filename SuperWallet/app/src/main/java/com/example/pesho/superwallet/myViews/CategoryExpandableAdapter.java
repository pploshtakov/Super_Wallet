package com.example.pesho.superwallet.myViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;

import java.util.List;

public class CategoryExpandableAdapter extends ExpandableRecyclerAdapter<TransactionsParentViewHolder, TransactionsChildViewHolder> {

    LayoutInflater mInflater;
    Context contect;

    public CategoryExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TransactionsParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_transactions_parent, viewGroup, false);
        return new TransactionsParentViewHolder(view);
    }

    @Override
    public TransactionsChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.list_item_transaction_child, viewGroup, false);
        return new TransactionsChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TransactionsParentViewHolder transactionsParentViewHolder, int i, Object parentListItem) {
        TransactionsListCategory category = (TransactionsListCategory) parentListItem;
        transactionsParentViewHolder.mCategoryTitleTextView.setText(category.getTitle());
    }

    @Override
    public void onBindChildViewHolder(TransactionsChildViewHolder transactionsChildViewHolder, int i, Object childObject) {
        TransactionsListTransaction transaction = (TransactionsListTransaction) childObject;
        transactionsChildViewHolder.mTransactionDateText.setText(transaction.getDate().toString());
    }
}
