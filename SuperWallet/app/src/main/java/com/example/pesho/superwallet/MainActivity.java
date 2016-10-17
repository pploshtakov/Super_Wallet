package com.example.pesho.superwallet;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.joda.time.LocalDateTime;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionMenu addTransactionButton;
    com.github.clans.fab.FloatingActionButton addIncomeFB;
    com.github.clans.fab.FloatingActionButton addExpenseFB;
    com.github.clans.fab.FloatingActionButton addTransferFB;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;

    AccountHeader header;
    Drawer drawer;

    private boolean init = false;
    private BoomMenuButton boomMenuButtonInActionBar;
    Bitmap profileImage;

    View mCustomView;
    TransactionsListFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsersManager.getInstance(this);


        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        //mTitleTextView.setText(R.string.home);
        mTitleTextView.setPadding( 16, 0, 0, 0);

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);
        boomMenuButtonInActionBar.setTextViewColor(ContextCompat.getColor(this, R.color.black));

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);



        mCurrentFragment = new TransactionsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.transactions_list, mCurrentFragment).commit();

        //create nav drawer

        profileImage = UsersManager.loggedUser.getProfileImage();
        if (profileImage == null) {
            Log.e("Profile", "//////");
        }
        if (profileImage != null) {
            header = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.header_background)
                    .addProfiles(
                            new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
                                    .withEmail(UsersManager.loggedUser.getEmail())
                                    .withIcon(profileImage)
                    )
                    .withSelectionListEnabledForSingleProfile(false)
                    .build();
        } else {
            header = new AccountHeaderBuilder()
                    .withActivity(this)
                    .withHeaderBackground(R.drawable.header_background)
                    .addProfiles(
                            new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
                                    .withEmail(UsersManager.loggedUser.getEmail())
                    )
                    .withSelectionListEnabledForSingleProfile(false)
                    .build();
        }

        drawer = new DrawerBuilder().withActivity(this).withCloseOnClick(true).withSliderBackgroundColorRes(R.color.secondPrimary).withAccountHeader(header).build();
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
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                switch (position) {
                    case 1:
                        mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_DAY, null);
                        drawer.closeDrawer();
                        break;
                    case 2:
                        mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_WEEK, null);
                        drawer.closeDrawer();
                        break;
                    case 3:
                        mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_MONTH, null);
                        drawer.closeDrawer();
                        break;
                    case 4:
                        mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_YEAR, null);
                        drawer.closeDrawer();
                        break;
                    case 5:
                        myCalendar = Calendar.getInstance();
                        new DatePickerDialog(MainActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                }

                return true;
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                LocalDateTime chooseDay = new LocalDateTime(myCalendar);
                Log.e("ChooseDay", chooseDay.toString());
                mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_CHOOSE_DAY, chooseDay);
                drawer.closeDrawer();
                //dateTV.setText(DateFormat.getInstance().format(myCalendar.getTime()).substring(0, 8));
            }
        };

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
                addTransactionButton.close(false);
            }
        });
        //add expense
        addExpenseFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionsActivity.class);
                intent.putExtra("Transaction", Transaction.TRANSACTIONS_TYPE.Expense.toString());
                startActivityForResult(intent, ADD_TRANSACTION);
                addTransactionButton.close(false);
            }
        });//add transfer
        addTransferFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, AddTransactionsActivity.class);
                intent.putExtra("Transaction", Transaction.TRANSACTIONS_TYPE.Transfer.toString());
                startActivityForResult(intent, ADD_TRANSACTION);
                addTransactionButton.close(false);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Toast.makeText(MainActivity.this, "stats", Toast.LENGTH_SHORT).show();
				Intent statisticsIntent = new Intent(this, StatisticsActivity.class);
				startActivity(statisticsIntent);
                return true;
//            case R.id.action_category:
//                // User chose the "Settings" item, show the app settings UI...
//                Intent category = new Intent(this, CategoryListActivity.class);
//                startActivity(category);
//                return true;
//            case R.id.action_accounts:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Intent accounts = new Intent(this, AccountsActivity.class);
//				accounts.putExtra("pickingAccount", false);
//                startActivity(accounts);
//                return true;
//            case R.id.action_settings:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
//        MenuItem itemView = menu.findItem(R.id.action_home);
//        itemView.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (boomMenuButtonInActionBar != null) {
            // Use a param to record whether the boom button has been initialized
            // Because we don't need to init it again when onResume()
            if (init) return;
            init = true;

            Drawable[] subButtonDrawables = new Drawable[3];
            int[] drawablesResource = new int[]{
                    R.drawable.category,
                    R.drawable.bill,
                    R.drawable.settings
            };
            for (int i = 0; i < 3; i++)
                subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

            String[] subButtonTexts = new String[]{"Category List", "Accounts List", "Settings"};

            int[][] subButtonColors = new int[][] {
                    { ContextCompat.getColor(this,R.color.accent), ContextCompat.getColor(this, R.color.accent) },
                    { ContextCompat.getColor(this,R.color.md_blue_600), ContextCompat.getColor(this, R.color.md_blue_600) },
                    { ContextCompat.getColor(this,R.color.md_red_600), ContextCompat.getColor(this, R.color.md_red_600) }
            };


            // Now with Builder, you can init BMB more convenient
            new BoomMenuButton.Builder()
                    .subButtons(subButtonDrawables, subButtonColors, subButtonTexts)
//                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.category), subButtonColors[0], "BoomMenuButton")
//                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.bill), subButtonColors[0], "View source code")
//                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.settings), subButtonColors[0], "Follow me")
                    .button(ButtonType.HAM)
                    .boom(BoomType.PARABOLA)
                    .place(PlaceType.HAM_3_1)
                    .subButtonTextColor(ContextCompat.getColor(this, R.color.md_blue_600))
                    .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                    .delay(50)
                    .duration(600)
                    .showOrder(OrderType.REVERSE)
                    .init(boomMenuButtonInActionBar).setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {

                @Override
                public void onClick(int buttonIndex) {
                    if (buttonIndex == 0) {
                        Intent intent = new Intent(MainActivity.this, CategoryListActivity.class);
                        startActivityForResult(intent, 0);
                    }
                    if (buttonIndex == 1) {
                        Intent intent = new Intent(MainActivity.this, AccountsActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }
            });

        }
        else {
            Log.e("SuperWallet ", "Boom Menu Button is NULL.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		mCurrentFragment.updateList();
        switch (requestCode) {
            case AddTransactionsActivity.RESULT_UPDATE_TRANSACTIN_LIST:
                mCurrentFragment.changePeriod(TransactionsListFragment.TIMEPERIOD_DAY, null);
                drawer.closeDrawer();
        }
    }
}
