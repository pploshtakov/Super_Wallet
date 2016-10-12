package com.example.pesho.superwallet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pesho.superwallet.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DBManager extends SQLiteOpenHelper {
    private static DBManager ourInstance;
    //DB version
    private static final int DB_VERSION = 3;
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
	private static final String KEY_ACCOUNT_ID = "accountId";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_ACCOUNT_BALANCE = "balance";
    private static final String KEY_ACCOUNT_TYPE = "accountType";
    private static final String KEY_ACCOUNT_USER_ID = "userID";
    //categories column names
    private static final String KEY_CATEGORIES_ID = "categoryId";
    private static final String KEY_CATEGORIES_NAME = "categoryName";
    private static final String KEY_CATEGORIES_TYPE = "categoryType";
    private static final String KEY_CATEGORIES_ICON = "categoryIcon";
	private static final String KEY_CATEGORIES_DESCRIPTION = "categoryDescription";
    private static final String KEY_CATEGORIES_USER_ID = "userID";
    //transactions column names
	private static final String KEY_TRANSACTION_ID = "transactionId";
    private static final String KEY_TRANSACTION_DATE = "transactionDate";
    private static final String KEY_TRANSACTION_DESCRIPTION = "transactionDescription";
    private static final String KEY_TRANSACTION_TYPE = "transactionType";
    private static final String KEY_TRANSACTION_AMOUNT = "transactionAmount";
    private static final String KEY_TRANSACTION_CATEGORY_ID = "categoryId";
	private static final String KEY_TRANSACTION_ACCOUNT_FROM_ID = "accountFromId";
	private static final String KEY_TRANSACTION_ACCOUNT_TO_ID = "accountToId";
    private static final String KEY_TRANSACTION_USER_ID = "userID";
    //table users create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_LOCAL_ID + " INTEGER PRIMARY KEY," + KEY_GOOGLE_ID + " TEXT," + KEY_FACEBOOK_ID
            + " TEXT," + KEY_NAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_EMAIL
            + " TEXT" + ")";
    //table accounts create statement
    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE "
            + TABLE_ACCOUNTS + "(" + KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY, " + KEY_ACCOUNT_NAME + " TEXT," + KEY_ACCOUNT_BALANCE + " REAL," + KEY_ACCOUNT_TYPE
            + " TEXT," + KEY_ACCOUNT_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_ACCOUNT_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";
    //table category create statement
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE "
            + TABLE_CATEGORIES + " (" + KEY_CATEGORIES_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORIES_NAME + " TEXT," + KEY_CATEGORIES_TYPE + " TEXT,"
            + KEY_CATEGORIES_ICON + " INTEGER," + KEY_CATEGORIES_DESCRIPTION + " TEXT," + KEY_CATEGORIES_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_CATEGORIES_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";
    //table default category create statement
    private static final String CREATE_TABLE_DEFAULT_CATEGORIES = "CREATE TABLE "
            + TABLE_DEFAULT_CATEGORIES + " (" + KEY_CATEGORIES_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORIES_NAME + " TEXT," + KEY_CATEGORIES_TYPE + " TEXT,"
            + KEY_CATEGORIES_ICON + " INTEGER)";
    //table transactions create statement
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "
            + TABLE_TRANSACTIONS + "(" + KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY," + KEY_TRANSACTION_DATE + " TIMESTAMP," + KEY_TRANSACTION_DESCRIPTION + " TEXT," + KEY_TRANSACTION_TYPE
            + " TEXT," + KEY_TRANSACTION_AMOUNT + " REAL," + KEY_TRANSACTION_CATEGORY_ID + " INTEGER," + KEY_TRANSACTION_ACCOUNT_FROM_ID + " INTEGER, " + KEY_TRANSACTION_ACCOUNT_TO_ID + " INTEGER, "
            + KEY_TRANSACTION_USER_ID + " INTEGER," + " FOREIGN KEY"
            + "(" + KEY_TRANSACTION_USER_ID + ")" + " REFERENCES " + TABLE_USERS + "(" + KEY_LOCAL_ID + ")" + ")";


	private static final String DROP_TABLE_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
	private static final String DROP_TABLE_DEFAULT_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
	private static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + TABLE_USERS;
	private static final String DROP_TABLE_ACCOUNTS = "DROP TABLE IF EXISTS " + TABLE_ACCOUNTS;
	private static final String DROP_TABLE_TRANSACTIONS = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS;



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
		db.execSQL(DROP_TABLE_USERS);
		db.execSQL(DROP_TABLE_ACCOUNTS);
		db.execSQL(DROP_TABLE_TRANSACTIONS);
		db.execSQL(DROP_TABLE_CATEGORIES);
		db.execSQL(DROP_TABLE_DEFAULT_CATEGORIES);

		this.onCreate(db);
    }

	public int getNextUserLocalId() {

		ArrayList<Integer> userIds = new ArrayList<>();
		Cursor cursor = getWritableDatabase().rawQuery("SELECT "
				+ KEY_LOCAL_ID + " FROM " + TABLE_USERS, null);
		try {
			while (cursor.moveToNext()) {
				int userId = cursor.getInt(cursor.getColumnIndex(KEY_LOCAL_ID));
				userIds.add(userId);
			}
		}
		finally {
			cursor.close();
		}
		if (userIds.size() == 0) {
			return 0;
		}
		else {
			int maxUserId = 0;
			for (Integer i: userIds) {
				if (i >= maxUserId) {
					maxUserId = i + 1;
				}
			}
			return maxUserId;
		}
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
			User user = new User(localID, googleID, facebookID, name, username, password, email);
            users.put(username, user);

			ArrayList<Account> accounts = loadAccountsForUser(localID);
			ArrayList<Category> categories = loadCategoriesForUser(localID);
			ArrayList<Transaction> transactions = loadTransactionsForUser(user, categories, accounts);

			user.setMyAccounts(accounts);
			user.setMyCategories(categories);
			user.setMyTransactions(transactions);

        }
        cursor.close();
        return users;
    }

	public int getNextAccountIndex() {

		ArrayList<Integer> accountIds = new ArrayList<>();
		Cursor cursor = getWritableDatabase().rawQuery("SELECT "
				+ KEY_ACCOUNT_ID + " FROM " + TABLE_ACCOUNTS, null);
		try {
			while (cursor.moveToNext()) {
				int accountId = cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNT_ID));
				accountIds.add(accountId);
			}
		}
		finally {
			cursor.close();
		}
		if (accountIds.size() == 0) {
			return 0;
		}
		else {
			int maxAccId = 0;
			for (Integer i: accountIds) {
				if (i >= maxAccId) {
					maxAccId = i + 1;
				}
			}
			return maxAccId;
		}
	}

    //add account in table accounts
    public void addAccount (Account account) {
        ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_ID, account.getAccountId());
        values.put(KEY_ACCOUNT_NAME, account.getAccountName());
        values.put(KEY_ACCOUNT_BALANCE, account.getAccountBalance());
        values.put(KEY_ACCOUNT_TYPE, account.getAccountType().toString());
        values.put(KEY_ACCOUNT_USER_ID, UsersManager.loggedUser.getLocalID());
        getWritableDatabase().insert(TABLE_ACCOUNTS, null, values);
    }

	//update category in table categories
	public void updateAccount(Account account) {
		ContentValues values = new ContentValues();
		values.put(KEY_ACCOUNT_NAME, account.getAccountName());
		values.put(KEY_ACCOUNT_BALANCE, account.getAccountBalance());
		values.put(KEY_ACCOUNT_TYPE, account.getAccountType().toString());
		getWritableDatabase().update(TABLE_ACCOUNTS, values, KEY_ACCOUNT_ID + "=" + account.getAccountId(), null);
	}

	public void deleteAccount(Account account) {
		getWritableDatabase().delete(TABLE_ACCOUNTS, KEY_ACCOUNT_ID + "=?", new String[] { String.valueOf(account.getAccountId()) });
	}

    //load accounts for user x from accounts
    public ArrayList<Account> loadAccountsForUser (int localID) {
        ArrayList<Account> accounts = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(localID)});
        while (cursor.moveToNext()) {
			int accountId = cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNT_ID));
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
            accounts.add(new Account(accountId, accountName, balance, type));
        }
        cursor.close();
        return accounts;
    }
	//add category in table categories
	public void addCategory(Category category) {
		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORIES_ID, category.getCategoryId());
		values.put(KEY_CATEGORIES_NAME, category.getCategoryName());
		values.put(KEY_CATEGORIES_TYPE, category.getTransactionType().toString());
		values.put(KEY_CATEGORIES_ICON, category.getCategoryIcon());
		values.put(KEY_CATEGORIES_DESCRIPTION, category.getCategoryDescription());
		values.put(KEY_CATEGORIES_USER_ID, UsersManager.loggedUser.getLocalID());
		getWritableDatabase().insert(TABLE_CATEGORIES, null, values);
	}

	//update category in table categories
	public void updateCategory(Category category) {
		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORIES_NAME, category.getCategoryName());
		values.put(KEY_CATEGORIES_TYPE, category.getTransactionType().toString());
		values.put(KEY_CATEGORIES_ICON, category.getCategoryIcon());
		values.put(KEY_CATEGORIES_DESCRIPTION, category.getCategoryDescription());
		getWritableDatabase().update(TABLE_CATEGORIES, values, KEY_CATEGORIES_ID + "=" + category.getCategoryId(), null);
	}

	public void deleteCategory(Category category) {
		getWritableDatabase().delete(TABLE_CATEGORIES, KEY_CATEGORIES_ID + "=?", new String[] { String.valueOf(category.getCategoryId()) });
	}

    //load categories for user x from categories
    public ArrayList<Category> loadCategoriesForUser(int localID) {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(localID)});
        while (cursor.moveToNext()) {
            int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ID));
            String categoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_NAME));
            String categoryType = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_TYPE));
            int categoryIcon = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ICON));
			String categoryDescription = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIES_DESCRIPTION));
            Transaction.TRANSACTIONS_TYPE type;
            if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Income;
            } else if (categoryType.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString())) {
                type = Transaction.TRANSACTIONS_TYPE.Expense;
            } else {
                type = Transaction.TRANSACTIONS_TYPE.Transfer;
            }
			Category cat = new Category(categoryId, type, categoryName, categoryIcon);
			cat.setCategoryDescription(categoryDescription);
            categories.add(cat);
			Log.e("SuperWallet DB Manager ", "CategoryIcon " + categoryIcon + " loaded for categoryId " + categoryId + ".");
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

		ArrayList<Integer> categoryIds = new ArrayList<>();
		Cursor cursor = getWritableDatabase().rawQuery("SELECT "
				+ KEY_CATEGORIES_ID + " FROM " + TABLE_CATEGORIES, null);
		try {
			while (cursor.moveToNext()) {
				int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIES_ID));
				categoryIds.add(categoryId);
			}
		}
		finally {
			cursor.close();
		}
		if (categoryIds.size() == 0) {
			return 0;
		}
		else {
			int maxCatId = 0;
			for (Integer i: categoryIds) {
				if (i >= maxCatId) {
					maxCatId = i + 1;
				}
			}
			return maxCatId;
		}
	}

	public int getNextTransactionIndex() {

		ArrayList<Integer> transactionIds = new ArrayList<>();
		Cursor cursor = getWritableDatabase().rawQuery("SELECT "
				+ KEY_TRANSACTION_ID + " FROM " + TABLE_TRANSACTIONS, null);
		try {
			while (cursor.moveToNext()) {
				int transactionId = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ID));
				transactionIds.add(transactionId);
			}
		}
		finally {
			cursor.close();
		}
		if (transactionIds.size() == 0) {
			return 0;
		}
		else {
			int maxTransactionId = 0;
			for (Integer i: transactionIds) {
				if (i >= maxTransactionId) {
					maxTransactionId = i + 1;
				}
			}
			return maxTransactionId;
		}
	}

	// TODO doesnt work for Transfer...
	public void addTransaction(Transfer transfer) {
		ContentValues values = new ContentValues();
		values.put(KEY_TRANSACTION_ID, transfer.getTransactionId());
		values.put(KEY_TRANSACTION_DATE, transfer.getDateAsSQLTimestamp().toString());
		values.put(KEY_TRANSACTION_DESCRIPTION, transfer.getDescription());
		values.put(KEY_TRANSACTION_TYPE, transfer.getTransactionType().toString());
		values.put(KEY_TRANSACTION_AMOUNT, transfer.getAmount());
		values.put(KEY_TRANSACTION_CATEGORY_ID, "");
		values.put(KEY_TRANSACTION_ACCOUNT_FROM_ID, transfer.getAccountFrom().getAccountId());
		values.put(KEY_TRANSACTION_ACCOUNT_TO_ID, transfer.getAccountTo().getAccountId());
		values.put(KEY_TRANSACTION_USER_ID, UsersManager.loggedUser.getLocalID());
		long result = getWritableDatabase().insert(TABLE_TRANSACTIONS, null, values);
	}

    //add transaction in table transactions
    public  void addTransaction(Transaction transaction) {

        ContentValues values = new ContentValues();
		values.put(KEY_TRANSACTION_ID, transaction.getTransactionId());
        values.put(KEY_TRANSACTION_DATE, transaction.getDateAsSQLTimestamp().toString());
        values.put(KEY_TRANSACTION_DESCRIPTION, transaction.getDescription());
        values.put(KEY_TRANSACTION_TYPE, transaction.getTransactionType().toString());
        values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
		if (transaction.getCategory() == null) {
			values.put(KEY_TRANSACTION_CATEGORY_ID, "");
		}
		else {
			values.put(KEY_TRANSACTION_CATEGORY_ID, transaction.getCategory().getCategoryId() );
		}
		values.put(KEY_TRANSACTION_ACCOUNT_FROM_ID, "-1");
		values.put(KEY_TRANSACTION_ACCOUNT_TO_ID, "-1");
        values.put(KEY_TRANSACTION_USER_ID, UsersManager.loggedUser.getLocalID());
        long result = getWritableDatabase().insert(TABLE_TRANSACTIONS, null, values);
    }

    //load transactions for user x from transactions
    public ArrayList<Transaction> loadTransactionsForUser (User user, ArrayList<Category> categories, ArrayList<Account> accounts) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + KEY_ACCOUNT_USER_ID + " = ?", new String[] {String.valueOf(user.getLocalID())});

		ArrayList<Category> defaultCategories = loadDefaultCategories();
        while (cursor.moveToNext()) {
			int transactionId = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ID));
            String date = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE));
            String description = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DESCRIPTION));
            String transactionType = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE));
            double amount = cursor.getDouble(cursor.getColumnIndex(KEY_TRANSACTION_AMOUNT));

			if (transactionType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString()) || transactionType.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString())) {
				Transaction.TRANSACTIONS_TYPE type = Transaction.TRANSACTIONS_TYPE.valueOf(transactionType);

				Category category = null;
				int categoryId = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_CATEGORY_ID));

				for (Category cat: defaultCategories) {
					if (cat.getCategoryId() == categoryId) {
						category = cat;
						break;
					}
				}

				if (category == null) {
					for (Category cat : categories) {
						if (cat.getCategoryId() == categoryId) {
							category = cat;
							break;
						}
					}
				}

				Transaction transaction = new Transaction(transactionId, Transaction.getDateFromSQLTimestamp(date), type, amount);
				transaction.setDescription(description);
				transaction.setCategory(category);
				transactions.add(transaction);
            } else {
				Account accountFrom = null;
				Account accountTo = null;

				int accountFromId = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ACCOUNT_FROM_ID));
				int accountToId = cursor.getInt(cursor.getColumnIndex(KEY_TRANSACTION_ACCOUNT_TO_ID));
				if (accountFromId == user.getDefaultAccount().getAccountId()) {
					accountFrom = user.getDefaultAccount();
				}
				if (accountToId == user.getDefaultAccount().getAccountId()) {
					accountTo = user.getDefaultAccount();
				}

				if (accountFrom == null || accountTo == null) {
					for (Account acc : accounts) {
						if (acc.getAccountId() == accountFromId) {
							accountFrom = acc;
							if (accountTo != null) {
								break;
							}
						}
						if (acc.getAccountId() == accountToId) {
							accountTo = acc;
							if (accountFrom != null) {
								break;
							}
						}
					}
				}

				if (accountFrom == null || accountTo == null) {
					continue;
				}

				Transaction transfer = new Transfer(transactionId, Transaction.getDateFromSQLTimestamp(date), amount, accountFrom, accountTo);
				transfer.setDescription(description);
				transactions.add(transfer);
            }
        }
		cursor.close();
		return transactions;
    }

}
