package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pesho.superwallet.model.UsersManager;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionMenu addTransactionButton;
    com.github.clans.fab.FloatingActionButton addIncomeFB;
    com.github.clans.fab.FloatingActionButton addExpenseFB;
    com.github.clans.fab.FloatingActionButton addTransferFB;

    AccountHeader header;


    //    TabLayout settingsTab;
//    TabLayout reportsTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TransactionsListFragment fragment = new TransactionsListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        //create nav drawer
        header = new AccountHeaderBuilder()
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

        addTransactionButton = (FloatingActionMenu) findViewById(R.id.main_fab);
        addIncomeFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_income);
        addExpenseFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_expense);
        addTransferFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_transfer);

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
        //add income
        addIncomeFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTrActivity.class);
                intent.putExtra("Transaction", "Income");
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });
        //add expense
        addExpenseFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTrActivity.class);
                intent.putExtra("Transaction", "Expense");
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });//add transfer
        addTransferFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTrActivity.class);
                intent.putExtra("Transaction", "Transfer");
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Toast.makeText(MainActivity.this, "stats", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_category:
                // User chose the "Settings" item, show the app settings UI...
                Intent category = new Intent(this, CategoryListActivity.class);
                startActivity(category);
                return true;
            case R.id.action_accounts:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
//                Intent accounts = new Intent(this, AccountsActivity.class);
//                startActivity(accounts);
//                return true;
				Intent category2 = new Intent(this, CategoryListActivity.class);
				category2.putExtra("pickingCategory", true);
				startActivity(category2);
				return true;
            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        MenuItem itemView = menu.findItem(R.id.action_home);
        itemView.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

}
