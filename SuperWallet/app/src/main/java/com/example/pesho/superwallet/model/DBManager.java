package com.example.pesho.superwallet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;

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
    //users column names
    private static final String KEY_LOCAL_ID = "localID";
    private static final String KEY_GOOGLE_ID = "googleID";
    private static final String KEY_FACEBOOK_ID = "facebookID";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    //table users create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_LOCAL_ID + " INTEGER PRIMARY KEY," + KEY_GOOGLE_ID + " TEXT," + KEY_FACEBOOK_ID
            + " TEXT," + KEY_NAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_EMAIL
            + " TEXT" + ")";






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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

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
            users.put(username, new User(localID, googleID, facebookID, name, username, password, email));
        }
        cursor.close();
        Log.e("Users", users.toString());
        return users;
    }
}
