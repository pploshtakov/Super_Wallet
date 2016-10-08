package com.example.pesho.superwallet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pesho.superwallet.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Pesho on 9/29/2016.
 */
public class DBManager extends SQLiteOpenHelper {
    private static DBManager ourInstance;
    //DB version
    private static final int DB_VERSION = 1;
    //DB name
    private static final String DB_NAME = "mySQLite";
    //table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ACCOUNTS = "accounts";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_DEFAULT_CATEGORIES = "categoriesDefault";
    private static final String TABLE_TRANSACTIONS = "transactions";
    //users column names
    private static final String KEY_LOCAL_ID = "localID";
    private static final String KEY_GOOGLE_ID = "googleID";
    private static final String KEY_FACEBOOK_ID = "facebookID";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    //accounts column names
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_ACCOUNT_BALANCE = "balance";
    private static final String KEY_ACCOUNT_TYPE = "accountType";
    private static final String KEY_ACCOUNT_USER_ID = "userID";
    //categories column names
    private static final String KEY_CATEGORIES_ID = "categoryId";
    private static final String KEY_CATEGORIES_NAME = "categoryName";
    private static final String KEY_CATEGORIES_TYPE = "categoryType";
    private static final String KEY_CATEGORIES_ICON = "categoryIcon";
    private static final String KEY_CATEGORIES_USER_ID = "userID";
    //transactions column names
    private static final String KEY_TRANSACTION_DATE = "date";
    private static final String KEY_TRANSACTION_DESCRIPTION = "description";
    private static final String KEY_TRANSACTION_TYPE = "transactionType";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_CATEGORY = "category";
    private static final String KEY_TRANSACTION_USER_ID = "userID";
    //table users create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_LOCAL_ID + " INTEGER PRIMARY KEY," + KEY_GOOGLE_ID + " TEXT," + KEY_FACEBOOK_ID
            + " TEXT," + KEY_NAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_EMAIL
            + " TEXT" + ")";
    //table accounts create statement
    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE "
            + TABLE_ACCOUNTS + "(" + KEY_ACCOUNT_NAME + " TEXT PRIMARY KEY," + KEY_ACCOUNT_BALANCE + " REAL," + KEY_ACCOUNT_TYPE
            + " TEXT," + KEY_ACCOUNT_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_ACCOUNT_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";
    //table category create statement
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE "
            + TABLE_CATEGORIES + "(" + KEY_CATEGORIES_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORIES_NAME + " TEXT," + KEY_CATEGORIES_TYPE + " TEXT,"
            + KEY_CATEGORIES_ICON + " INTEGER," + KEY_CATEGORIES_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_CATEGORIES_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";
    //table default category create statement
    private static final String CREATE_TABLE_DEFAULT_CATEGORIES = "CREATE TABLE "
            + TABLE_DEFAULT_CATEGORIES + "(" + KEY_CATEGORIES_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORIES_NAME + " TEXT," + KEY_CATEGORIES_TYPE + " TEXT,"
            + KEY_CATEGORIES_ICON + " INTEGER)";
    //table transactions create statement
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "
            + TABLE_TRANSACTIONS + "(" + KEY_TRANSACTION_DATE + " TEXT," + KEY_TRANSACTION_DESCRIPTION + " TEXT," + KEY_TRANSACTION_TYPE
            + " TEXT," + KEY_TRANSACTION_AMOUNT + " REAL," + KEY_TRANSACTION_CATEGORY + " TEXT,"
            + KEY_TRANSACTION_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_TRANSACTION_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";




    public static DBManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBManager(context);
        }
        return ourInstance;
    }

    private DBManager(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ACCOUNTS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_DEFAULT_CATEGORIES);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);

        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORIES_ID, -1);
        values.put(KEY_CATEGORIES_NAME, "Home");
        values.put(KEY_CATEGORIES_ICON, R.drawable.house);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -2);
        values.put(KEY_CATEGORIES_NAME, "Sports");
        values.put(KEY_CATEGORIES_ICON, R.drawable.ball);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -3);
        values.put(KEY_CATEGORIES_NAME, "Bills");
        values.put(KEY_CATEGORIES_ICON, R.drawable.bill);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -4);
        values.put(KEY_CATEGORIES_NAME, "Public Transport");
        values.put(KEY_CATEGORIES_ICON, R.drawable.bus);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -5);
        values.put(KEY_CATEGORIES_NAME, "Car");
        values.put(KEY_CATEGORIES_ICON, R.drawable.car);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -6);
        values.put(KEY_CATEGORIES_NAME, "Restaurant");
        values.put(KEY_CATEGORIES_ICON, R.drawable.fork_spoon);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -7);
        values.put(KEY_CATEGORIES_NAME, "Coffee");
        values.put(KEY_CATEGORIES_ICON, R.drawable.coffee);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -8);
        values.put(KEY_CATEGORIES_NAME, "Fitness");
        values.put(KEY_CATEGORIES_ICON, R.drawable.fitness);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -9);
        values.put(KEY_CATEGORIES_NAME, "Fridge");
        values.put(KEY_CATEGORIES_ICON, R.drawable.fridge);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -10);
        values.put(KEY_CATEGORIES_NAME, "Healthcare");
        values.put(KEY_CATEGORIES_ICON, R.drawable.health);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -11);
        values.put(KEY_CATEGORIES_NAME, "Phone");
        values.put(KEY_CATEGORIES_ICON, R.drawable.phone);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);

        values.put(KEY_CATEGORIES_ID, -12);
        values.put(KEY_CATEGORIES_NAME, "Taxi");
        values.put(KEY_CATEGORIES_ICON, R.drawable.taxi);
        values.put(KEY_CATEGORIES_TYPE, Transaction.TRANSACTIONS_TYPE.Expense.toString());
        db.insert(TABLE_DEFAULT_CATEGORIES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //add user in table users
    public void addUser (int localID, String googleID, String facebookID, String name, String username, String password, String email ) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOCAL_ID, localID);
        values.put(KEY_GOOGLE_ID, googleID);
        values.put(KEY_FACEBOOK_ID, facebookID);
        values.put(KEY_NAME, name);
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_EMAIL, email);
        getWritableDatabase().insert(TABLE_USERS, null, values);
    }

    //load users from table users
    public HashMap<String, User> loadUsers() {
        HashMap<String, User> users = new HashMap<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT " + KEY_LOCAL_ID + ", " + KEY_GOOGLE_ID + ", " + KEY_FACEBOOK_ID + ", " + KEY_NAME +
        ", " + KEY_USERNAME + ", " + KEY_PASSWORD + ", " + KEY_EMAIL + " FROM " + TABLE_USERS, null);
        while (cursor.moveToNext()) {
            int localID = cursor.getInt(cursor.getColumnIndex(KEY_LOCAL_ID));
            String googleID = cursor.getString(cursor.getColumnIndex(KEY_GOOGLE_ID));
            String facebookID = cursor.getString(cursor.getColumnIndex(KEY_FACEBOOK_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            String username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL));
            ArrayList<Account> accounts = loadAccountsForUser(localID);
            ArrayList<Category> categories = loadCategoriesForUser(localID);
            ArrayList<Transaction> transactions = loadTransactionsForUser(localID);
            users.put(username, new User(localID, googleID, facebookID, name, username, password, email,
                    transactions,accounts,categories));
        }
        cursor.close();
        return users;
    }

    //add account in table accounts
    public void addAccount (String accountName, double balance, Account.ACCOUNT_TYPE accountType ) {
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_NAME, accountName);
        values.put(KEY_ACCOUNT_BALANCE, balance);
        values.put(KEY_ACCOUNT_TYPE, accountType.toString());
        values.put(KEY_ACCOUNT_USER_ID, UsersManager.loggedUser.getLocalID());
        getWritableDatabase().insert(TABLE_ACCOUNTS, null, values);
    }
    //load accounts for user x from accounts
    public ArrayList<Account> loadAccountsForUser (int localID) {
        ArrayList<Account> accounts = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT "
                + KEY_ACCOUNT_NAME + ", " + KEY_ACCOUNT_BALANCE + ", " + KEY_ACCOUNT_TYPE
                + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(localID)});
        while (cursor.moveToNext()) {
            String accountName = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_BALANCE));
            String accountType = cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_TYPE));
            Account.ACCOUNT_TYPE type;
            if (accountType.equals(Account.ACCOUNT_TYPE.ACCOUNT.toString())) {
                type = Account.ACCOUNT_TYPE.ACCOUNT;
            } else if (accountType.equals(Account.ACCOUNT_TYPE.CARD.toString())) {
                type = Account.ACCOUNT_TYPE.CARD;
            } else {
                type = Account.ACCOUNT_TYPE.CASH;
            }
            accounts.add(new Account(accountName, balance, type));
        }
        cursor.close();
        return accounts;
    }
    //add category in table categories
    public void addCategory(Transaction.TRANSACTIONS_TYPE transactionType, String categoryName) {
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORIES_NAME, categoryName);
        values.put(KEY_CATEGORIES_TYPE, transactionType.toString());
        values.put(KEY_CATEGORIES_USER_ID, UsersManager.loggedUser.getLocalID());
        getWritableDatabase().insert(TABLE_CATEGORIES, null, values);
    }
    //load categories for user x from categories
    public ArrayList<Category> loadCategoriesForUser(int localID) {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT "
                + KEY_CATEGORIES_ID + ", " + KEY_CATEGORIES_NAME + ", " + KEY_CATEGORIES_TYPE + "," + KEY_CATEGORIES_ICON + " FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(localID)});
        while (cursor.moveToNext()) {
            int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ID));
            String categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_NAME));
            String categoryType = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_TYPE));
            int categoryIcon = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ICON));
            Transaction.TRANSACTIONS_TYPE type;
            if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Income;
            } else if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Expense;
            } else {
                type = Transaction.TRANSACTIONS_TYPE.Transfer;
            }
            categories.add(new Category(categoryId, type, categoryName, categoryIcon));
        }
        cursor.close();
        return categories;
    }

    //load categories for user x from categories
    public ArrayList<Category> loadDefaultCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT "
                + KEY_CATEGORIES_ID + ", " + KEY_CATEGORIES_NAME + ", " + KEY_CATEGORIES_TYPE + "," + KEY_CATEGORIES_ICON + " FROM " + TABLE_DEFAULT_CATEGORIES, null);
        while (cursor.moveToNext()) {
            int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ID));
            String categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_NAME));
            String categoryType = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_TYPE));
            int categoryIcon = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ICON));
            Transaction.TRANSACTIONS_TYPE type;
            if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Income;
            } else if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Expense;
            } else {
                type = Transaction.TRANSACTIONS_TYPE.Transfer;
            }
            categories.add(new Category(categoryId, type, categoryName, categoryIcon));
        }
        cursor.close();
        return categories;
    }

	public int getNextUserCategoryIndex() {
		int categoryId = 0;
		Cursor cursor = getWritableDatabase().rawQuery("SELECT MAX("
				+ KEY_CATEGORIES_ID + ") FROM " + TABLE_CATEGORIES, null);
		if (cursor.moveToFirst()) {
			categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ID)) + 1;
		}
		cursor.close();
		return categoryId;
	}

    //add transaction in table transactions
    public void addTransaction(String date, String description, Transaction.TRANSACTIONS_TYPE transactionType,
                               double amount, Category category) {
        ContentValues values = new ContentValues();
        values.put(KEY_TRANSACTION_DATE, date);
        values.put(KEY_TRANSACTION_DESCRIPTION, description);
        values.put(KEY_TRANSACTION_TYPE, transactionType.toString());
        values.put(KEY_TRANSACTION_AMOUNT, amount);
        values.put(KEY_TRANSACTION_CATEGORY, category.getCategoryName());
        values.put(KEY_TRANSACTION_USER_ID, UsersManager.loggedUser.getLocalID());
        getWritableDatabase().insert(TABLE_TRANSACTIONS, null, values);
    }
    //load transactions for user x from transactions
    public ArrayList<Transaction> loadTransactionsForUser (int localID) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT "
                + KEY_TRANSACTION_DATE + ", " + KEY_TRANSACTION_DESCRIPTION + ", " + KEY_TRANSACTION_TYPE
                +  ", " + KEY_TRANSACTION_AMOUNT +  ", " + KEY_TRANSACTION_CATEGORY  + " FROM " + TABLE_TRANSACTIONS
                + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(localID)});
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE));
            String description = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DESCRIPTION));
            String transactionType = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE));
            double amount = cursor.getDouble(cursor.getColumnIndex(KEY_TRANSACTION_AMOUNT));
            String categoryName = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_CATEGORY));
            Transaction.TRANSACTIONS_TYPE type;
            Category category = UsersManager.loggedUser.getCategory(categoryName);
            if (transactionType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Income;
            } else if (transactionType.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Expense;
            } else {
                type = Transaction.TRANSACTIONS_TYPE.Transfer;
            }
            transactions.add(new Transaction(date, description, type, amount, category));
        }
        cursor.close();
        return transactions;
    }
}
