package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionMenu addTransactionButton;
    com.github.clans.fab.FloatingActionButton addIncomeFB;
    com.github.clans.fab.FloatingActionButton addExpenseFB;
    com.github.clans.fab.FloatingActionButton addTransferFB;

    AccountHeader header;

    private FrameLayout transactionsListFL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransactionsListFragment mCurrentFragment = new TransactionsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.transactions_list, mCurrentFragment).commit();

        //create nav drawer
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
                                .withEmail(UsersManager.loggedUser.getEmail())
                                //.withIcon(UsersManager.loggedUser.getPhoto)
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();
        Drawer drawer = new DrawerBuilder().withActivity(this).withCloseOnClick(true).withSliderBackgroundColorRes(R.color.secondPrimary).withAccountHeader(header).build();
        //create drawer's items
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withTextColorRes(R.color.white).withName(R.string.drawer_item_day);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withTextColorRes(R.color.white).withName(R.string.drawer_item_week);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withTextColorRes(R.color.white).withName(R.string.drawer_item_month);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withTextColorRes(R.color.white).withName(R.string.drawer_item_year);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withTextColorRes(R.color.white).withName(R.string.drawer_item_choose_date);
        //add drawer items
        drawer.addItem(item1);
        drawer.addItem(item2);
        drawer.addItem(item3);
        drawer.addItem(item4);
        drawer.addItem(item5);

        //drawer item selected listener
        /*
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                TransactionsListFragment fragment = ((MainActivity.ScreenSlidePagerAdapter)mPagerAdapter).getCurrentFragment(mPager.getCurrentItem());
                LocalDateTime startDate = new LocalDateTime();
                LocalDateTime endDate = new LocalDateTime();
                switch (position) {
                    case 1:
                        startDate = startDate.withTime(0,0,0,0);
                        endDate = endDate.plusDays(1).withTime(0,0,0,0);
                        break;
                    case 2:
                        startDate = startDate.minusDays(startDate.getDayOfWeek() - 1);
                        endDate = endDate.plusDays(8 - endDate.getDayOfWeek());
                        break;
                    case 3:
                        startDate = startDate.minusDays(startDate.getDayOfMonth() - 1);
                        endDate = endDate.plusDays(32 - endDate.getDayOfMonth());
                        break;
                    case 4:
                        startDate = startDate.minusDays(startDate.getDayOfYear() - 1);
                        endDate = endDate.plusDays(366 - endDate.getDayOfYear());
                        break;
                    case 5:
                        Toast.makeText(MainActivity.this, "Date", Toast.LENGTH_SHORT).show();
                        return true;
                }
                fragment.refreshList(startDate, endDate);
                mPagerAdapter.notifyDataSetChanged();
                return true;
            }
        });
        */

        //adding floating buttons
        addTransactionButton = (FloatingActionMenu) findViewById(R.id.main_fab);
        addIncomeFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_income);
        addExpenseFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_expense);
        addTransferFB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_transfer);

        //floating action button - add transaction
        //add income
        addIncomeFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionsActivity.class);
                intent.putExtra("Transaction", Transaction.TRANSACTIONS_TYPE.Income.toString());
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });
        //add expense
        addExpenseFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionsActivity.class);
                intent.putExtra("Transaction", Transaction.TRANSACTIONS_TYPE.Expense.toString());
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });//add transfer
        addTransferFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionsActivity.class);
                intent.putExtra("Transaction", Transaction.TRANSACTIONS_TYPE.Transfer.toString());
                startActivityForResult(intent, ADD_TRANSACTION);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Toast.makeText(MainActivity.this, "stats", Toast.LENGTH_SHORT).show();
				Intent statisticsIntent = new Intent(this, PieStatisticsActivity.class);
				startActivity(statisticsIntent);
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
				accounts.putExtra("pickingAccount", false);
                startActivity(accounts);
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
