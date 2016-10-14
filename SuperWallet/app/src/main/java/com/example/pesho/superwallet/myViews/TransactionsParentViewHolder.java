package com.example.pesho.superwallet.myViews;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.pesho.superwallet.R;

public class TransactionsParentViewHolder extends ParentViewHolder {

    public TextView mCategoryTitleTextView;
    public ImageButton mParentDropDownArrow;

    public TransactionsParentViewHolder(View itemView) {
        super(itemView);

        mCategoryTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_category_title_text_view);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_list_item_expand_arrow);
    }

}
