package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.pesho.superwallet.model.UsersManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionButton addTransactionButton;
    AccountHeader header;


    //    TabLayout settingsTab;
//    TabLayout reportsTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create nav drawer

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorAccent)
                .addProfiles(
                        new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
                                .withEmail(UsersManager.loggedUser.getEmail())
                                //.withIcon(UsersManager.loggedUser.getPhoto)
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();
        Drawer drawer = new DrawerBuilder().withActivity(this).withCloseOnClick(true).withSliderBackgroundColorRes(R.color.myGreen).withAccountHeader(header).build();
        //create drawer's items
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withTextColorRes(R.color.white).withName(R.string.drawer_item_day);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_week);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_month);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_year);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_choose_date);
        //add drawer items
        drawer.addItem(item1);
        drawer.addItem(item2);
        drawer.addItem(item3);
        drawer.addItem(item4);
        drawer.addItem(item5);





        addTransactionButton = (FloatingActionButton) findViewById(R.id.main_fab);
        //sorting reports tab
//        reportsTab = (TabLayout) findViewById(R.id.ma_reports_tab);
//        reportsTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch (tab.getText().toString()) {
//                    case "Daily":
//                    case "Weekly":
//                    case "Monthly":
//                    case "Year":
//                }
//            }
//
//                @Override
//                public void onTabUnselected(TabLayout.Tab tab) {
//
//                }
//
//                @Override
//                public void onTabReselected(TabLayout.Tab tab) {
//
//                }
//            });
//        //tab layout bottom of main screen
//        settingsTab = (TabLayout) findViewById(R.id.ma_settings_tab);
//        settingsTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                //switch tabs by name
//                switch (tab.getText().toString()) {
//                    case "Bills":
//                    case "Stats":
//                    case "Accounts":
//                        Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
//                        startActivity(intent);
//                        break;
//                    case "Profile":
//                    case "Settings":
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        //floating action button - add transaction
        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
