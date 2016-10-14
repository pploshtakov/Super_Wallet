package com.example.pesho.superwallet.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class TransactionsListCategory implements ParentObject {

    private String title;

    /* Create an instance variable for your list of children */
    private List<Object> mChildrenList;

    public TransactionsListCategory(String title) {
        this.title = title;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
