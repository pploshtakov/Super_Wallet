package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Toast.makeText(AccountsActivity.this, "stats", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                finish();
                return true;
            case R.id.action_category:
                // User chose the "Settings" item, show the app settings UI...
                Intent category = new Intent(this, CategoryListActivity.class);
                startActivity(category);
                return true;
            case R.id.action_accounts:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent accounts = new Intent(this, AccountsActivity.class);
                startActivity(accounts);
                return true;
            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(AccountsActivity.this, "settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        MenuItem itemView = menu.findItem(R.id.action_accounts);
        itemView.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }
}
