package com.example.pesho.superwallet.model.users;

import android.net.Uri;

import java.net.URI;

/**
 * Created by Pesho on 9/25/2016.
 */
public class User {
    private String name;
    private String email;
    private String id;
    private Uri photoURL;

    public User(String name, String email, String id, Uri photoURL) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.photoURL = photoURL;
    }
}
