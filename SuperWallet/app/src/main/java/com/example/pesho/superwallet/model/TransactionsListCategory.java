package com.example.pesho.superwallet.model;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

public class TransactionsListCategory implements Parent<TransactionsListTransaction> {

    private String title;

    /* Create an instance variable for your list of children */
    private List<TransactionsListTransaction> mChildrenList;

    public TransactionsListCategory(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setChildItemList(List<TransactionsListTransaction> childrenList) {
        this.mChildrenList = childrenList;
    }

    @Override
    public List<TransactionsListTransaction> getChildList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
