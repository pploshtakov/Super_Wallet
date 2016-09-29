package com.example.pesho.superwallet.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Pesho on 9/25/2016.
 */
public class User {
    private String name;
    private String email;
    //each user must have localID and googleID, facebookID or both of them;
    private String localID; //local id is the PrimaryKey in DB usersTable (0,1,2,3....)
    private String googleID;
    private String facebookID;
    private Uri photoURL;
    private ArrayList<Transaction> myTransactions;
    private ArrayList<Account> myAccounts;

    //constructor for users registered directly from app
    public User(String name, String email) {
        //TODO generate local id
        this.name = name;
        this.email = email;
        myTransactions = new ArrayList<>();
        myAccounts = new ArrayList<>();
    }
    //constructor for google user
    public User(String name, String email, String googleID, Uri photoURL) {
        //TODO generate local id
        this.name = name;
        this.email = email;
        this.googleID = googleID;
        this.photoURL = photoURL;
        myTransactions = new ArrayList<>();
        myAccounts = new ArrayList<>();
    }
    //constructor for facebook user
    public User(String name, String email, Uri photoURL, String facebookID) {
        //TODO generate local id
        this.name = name;
        this.email = email;
        this.facebookID = facebookID;
        this.photoURL = photoURL;
        myTransactions = new ArrayList<>();
        myAccounts = new ArrayList<>();
    }
}
