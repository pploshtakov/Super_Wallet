package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.User;
import com.example.pesho.superwallet.model.UsersManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10 ;
    private static final int REQUEST_FOR_REGISTER = 11 ;
    private GoogleApiClient mGoogleApiClient;
    private Button loginButton;
    private Button registerButton;
    private EditText usernameET;
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsersManager.getInstance(this);
        loginButton = (Button) findViewById(R.id.lp_login_button);
        registerButton = (Button) findViewById(R.id.lp_register_button);
        usernameET = (EditText) findViewById(R.id.lp_username_ET);
        passwordET = (EditText) findViewById(R.id.lp_password_ET);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        //TODO
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(LoginActivity.this, MainActivity.class);
                if (UsersManager.checkPass(usernameET.getText().toString(), passwordET.getText().toString())) {
                    login.putExtra("loggedUser", usernameET.getText().toString());
                    startActivity(login);
                    finish();
                } else {
                    passwordET.setError("Wrong username or password!");
                    passwordET.requestFocus();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(reg,REQUEST_FOR_REGISTER);
            }
        });
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GoogleLogin", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("login", acct.getId());
            Log.e("login", acct.getDisplayName());
            Log.e("login", acct.getGrantedScopes().toString());
            Log.e("login", acct.getPhotoUrl().toString());
            if (!UsersManager.isUserExistByGoogleID(acct.getId())) {
                User u = new User(acct.getDisplayName(),acct.getEmail(),acct.getId(),acct.getPhotoUrl());
                UsersManager.addUser(u);
            }
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("loggedUserID", acct.getId());
            startActivity(intent);
            finish();
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if(requestCode == REQUEST_FOR_REGISTER && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("userName")) {
                usernameET.setText(data.getExtras().get("userName").toString());
            }
        }
    }

}
