package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionButton addTransactionButton;
    TabLayout settingsTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTransactionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        //tab layout bottom of main screen
        settingsTab = (TabLayout) findViewById(R.id.ma_settings_tab);
        settingsTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //switch tabs by name
                switch (tab.getText().toString()) {
                    case "Accounts":
                        Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
                        startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //floating action button - add transaction
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });
    }
}
