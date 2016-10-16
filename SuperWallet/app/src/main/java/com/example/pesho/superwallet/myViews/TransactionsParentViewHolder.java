package com.example.pesho.superwallet.myViews;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;

public class TransactionsParentViewHolder extends ParentViewHolder<TransactionsListCategory, TransactionsListTransaction> {

    public TextView mCategoryTitleTextView;

    public TransactionsParentViewHolder(View itemView) {
        super(itemView);

        mCategoryTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_category_title_text_view);
    }

}
