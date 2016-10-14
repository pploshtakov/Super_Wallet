package com.example.pesho.superwallet.myViews;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.pesho.superwallet.R;

public class TransactionsChildViewHolder extends ChildViewHolder{

    public TextView mTransactionDateText;

    public TransactionsChildViewHolder(View itemView) {
        super(itemView);

        mTransactionDateText = (TextView) itemView.findViewById(R.id.child_list_item_transaction_date_text_view);
    }

}
