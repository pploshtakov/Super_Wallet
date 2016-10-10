package com.example.pesho.superwallet.model;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Pesho on 9/25/2016.
 */
public class User {
    private String name;
    private String userName;
    private String password;
    private String email;
    //each user must have localID and googleID, facebookID or both of them;
    private int localID; //local id is the PrimaryKey in DB usersTable (0,1,2,3....)
    private String googleID;
    private String facebookID;
    private ArrayList<Transaction> myTransactions;
    private ArrayList<Account> myAccounts;
	private Account defaultAccount;
    private ArrayList<Category> myCategories;

	public User(String name, String email) {
		this.name = name;
		this.email = email;

		myTransactions = new ArrayList<>();
		myAccounts = new ArrayList<>();
		myCategories = new ArrayList<>();

		defaultAccount = new Account(-2, "Cash", 0.0, Account.ACCOUNT_TYPE.CASH);
		Log.e("SuperWallet ", "DefaultAccount: " + defaultAccount.getAccountName());
	}

    //constructor for users registered directly from app
    public User(String name, String userName, String password, String email) {
		this(name, email);

        this.localID = UsersManager.generateLocalID();
        this.userName = userName;
        this.password = password;
    }
    //constructor for google user
    public User(String name, String email, String googleID, Uri photoURL) {
		this(name, email);

        this.localID = UsersManager.generateLocalID();
        this.userName = name;
        this.googleID = googleID;
    }
    //constructor for facebook user
    public User(String name, String email, String facebookID) {
		this(name, email);

        this.localID = UsersManager.generateLocalID();
        this.userName = name;
        this.facebookID = facebookID;
    }

    //constructor from DB
    public User(int localID, String googleID, String facebookID, String name, String userName, String password,
                String email, ArrayList<Transaction> transactions, ArrayList<Account> accounts, ArrayList<Category> categories) {
		this(name, email);

        this.localID = localID;
        this.userName = userName;
        this.password = password;
        this.facebookID = facebookID;
        this.googleID = googleID;
        myTransactions = transactions;
        myAccounts = accounts;
        myCategories = categories;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public int getLocalID() {
        return localID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getPassword() {
        return password;
    }

    public Category getCategory(String categoryName) {
        for (int i = 0; i < myCategories.size(); i++) {
            if (myCategories.get(i).getCategoryName().equals(categoryName)) {
                return myCategories.get(i);
            }
        }
        return null;
    }

	public Category getCategory(int categoryId) {
		for (int i = 0; i < myCategories.size(); i++) {
			if (myCategories.get(i).getCategoryId() == categoryId) {
				return myCategories.get(i);
			}
		}
		return null;
	}

	public Account getAccount(int accountId) {
		for (int i = 0; i < myAccounts.size(); i++) {
			if (myAccounts.get(i).getAccountId() == accountId) {
				return myAccounts.get(i);
			}
		}
		return null;
	}

	public void addCategory(Category category) {
		myCategories.add(category);
	}

	public ArrayList<Category> getCategories() {
		return new ArrayList<>(myCategories);
	}

	public Account getDefaultAccount() { return defaultAccount; }
    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(myAccounts);
    }
    public void addAccount (Account acct) {
        myAccounts.add(acct);
    }
}
