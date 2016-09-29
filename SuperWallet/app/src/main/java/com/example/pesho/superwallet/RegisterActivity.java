package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pesho.superwallet.model.User;
import com.example.pesho.superwallet.model.UsersManager;

public class RegisterActivity extends AppCompatActivity {
    Button register;
    Button cancel;
    EditText name;
    EditText userName;
    EditText pass;
    EditText repeatPass;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = (Button) findViewById(R.id.rp_register_button);
        cancel = (Button) findViewById(R.id.rp_cancel_button);
        name = (EditText) findViewById(R.id.rp_name_ET);
        userName = (EditText) findViewById(R.id.rp_username_ET);
        pass = (EditText) findViewById(R.id.rp_pass1_ET);
        repeatPass = (EditText) findViewById(R.id.rp_pass2_ET);
        email = (EditText) findViewById(R.id.rp_email_ET);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getText().toString().trim();
                String userName1 = userName.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                if (UsersManager.isUserExistByName(userName1)|| userName1.isEmpty()) {
                    userName.setError("Username isn't available!");
                    return;
                } else if (name1.isEmpty()) {
                    name.setError("Please enter a name!");
                    return;
                } else if (!pass1.equals(repeatPass.getText().toString())|| pass1.isEmpty()) {
                    repeatPass.setError("The passwords don't match!");
                    //TODO check for weak password
                    return;
                } else if (email1.isEmpty() || !email1.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                    email.setError("Incorrect email!");
                    return;
                } else {

                    User user = new User(name1, userName1, pass1, email1);
                    UsersManager.addUser(user);
                    Intent intent = new Intent();
                    intent.putExtra("userName", userName1);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
