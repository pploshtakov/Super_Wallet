package com.example.pesho.superwallet.model;

/**
 * Created by Pesho on 9/25/2016.
 */
public class UsersManager {
    private static UsersManager ourInstance = new UsersManager();

    public static UsersManager getInstance() {
        return ourInstance;
    }

    private UsersManager() {
    }

    public static boolean isUserExist(String userId) {
        //TODO check in DB for user
        return false;
    }
}
