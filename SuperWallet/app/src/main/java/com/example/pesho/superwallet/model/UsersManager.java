package com.example.pesho.superwallet.model;

import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Pesho on 9/25/2016.
 */
public class UsersManager {
    private static UsersManager ourInstance;
    private static HashMap<String, User> users;
    private static Context context;
    public static User loggedUser;

    public static UsersManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new UsersManager(context);
        }
        return ourInstance;
    }

    private UsersManager(Context context) {
        this.context = context;
        users = new HashMap<>();
        users = DBManager.getInstance(context).loadUsers();
    }

    public static boolean isUserExistByName(String username) {
        return users.containsKey(username);
    }

    public static boolean isUserExistByGoogleID(String googleID) {
        for (User u: users.values()) {
            if (u.getGoogleID() != null && u.getGoogleID().equals(googleID)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isUserExistByFacebookID(String facebookID) {
        for (User u: users.values()) {
            if (u.getFacebookID() != null && u.getFacebookID().equals(facebookID)) {
                return true;
            }
        }
        return false;
    }

    public static void addUser(User u) {
        users.put(u.getUserName(), u);
        DBManager.getInstance(context).addUser(u.getLocalID(), u.getGoogleID(), u.getFacebookID(), u.getName(), u.getUserName(), u.getPassword(), u.getEmail());
    }

    public static int generateLocalID() {
        return users.size() + 1;
    }

    public static boolean checkPass(String username, String password) {
        if (isUserExistByName(username)) {
            User u = users.get(username);
            return u.getPassword().equals(password);
        }
        return false;
    }

    public static void setLoggedUser(String username) {
        loggedUser = users.get(username);
    }
}
