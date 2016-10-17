package com.example.pesho.superwallet.myViews;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.model.TransactionsListTransaction;

public class TransactionsChildViewHolder extends ChildViewHolder<TransactionsListTransaction> {

    public TextView mTransactionDateText;
    public TextView getmTransactionAmountText;
    public View root;

    public TransactionsChildViewHolder(View itemView) {
        super(itemView);

        mTransactionDateText = (TextView) itemView.findViewById(R.id.child_list_item_transaction_date_text_view);
        getmTransactionAmountText = (TextView) itemView.findViewById(R.id.child_list_item_transaction_amount_text_view);
        root = itemView;
    }

}
