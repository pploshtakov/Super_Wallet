package com.example.pesho.superwallet;

import android.content.Intent;

import android.os.AsyncTask;
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
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.io.InputStream;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION = 20;
    FloatingActionMenu addTransactionButton;
    com.github.clans.fab.FloatingActionButton addIncomeFB;
    com.github.clans.fab.FloatingActionButton addExpenseFB;
    com.github.clans.fab.FloatingActionButton addTransferFB;

    AccountHeader header;

    private boolean init = false;
    private BoomMenuButton boomMenuButtonInActionBar;
    Drawable profileImage;

    View mCustomView;

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
        mTitleTextView.setText(R.string.home);

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);



        TransactionsListFragment mCurrentFragment = new TransactionsListFragment();
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
                    R.drawable.house,
                    R.drawable.bill,
                    R.drawable.fork_spoon
            };
            for (int i = 0; i < 3; i++)
                subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

            String[] subButtonTexts = new String[]{"BoomMenuButton", "View source code", "Follow me"};

            int[][] subButtonColors = new int[3][2];
            for (int i = 0; i < 3; i++) {
                subButtonColors[i][1] = ContextCompat.getColor(this, R.color.white);
                subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
            }


            // Now with Builder, you can init BMB more convenient
            new BoomMenuButton.Builder()
                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.category), subButtonColors[0], "BoomMenuButton")
                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.bill), subButtonColors[0], "View source code")
                    .addSubButton(ContextCompat.getDrawable(this, R.drawable.settings), subButtonColors[0], "Follow me")
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
                    Log.e("SuperWallet ", "Button index: " + buttonIndex);
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

            Log.e("SuperWallet ", "Boom Menu Button !!!NOT NULL!!!");
        }
        else {
            Log.e("SuperWallet ", "Boom Menu Button is NULL.");
        }
    }



}
