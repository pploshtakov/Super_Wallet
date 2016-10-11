package com.example.pesho.superwallet;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pesho.superwallet.interfaces.AddTransactionsCommunicator;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import java.util.ArrayList;
import java.util.Date;

import github.chenupt.springindicator.SpringIndicator;

public class AddTransactionsActivity extends FragmentActivity implements AddTransactionsCommunicator {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    SpringIndicator springIndicator;
    String transactionsType;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    ArrayList<Fragment> fragments;
    ArrayList<Account> accounts;
    private Category category;
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private String description;
    private Date transactionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transactions);
        //get transaction's type
        Intent intent = getIntent();
        if (intent.hasExtra("Transaction")) {
            transactionsType = intent.getStringExtra("Transaction");
        }
        //load accounts
        accounts = new ArrayList<>();
        accountFrom = UsersManager.loggedUser.getDefaultAccount();
        if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Transfer)) {
            accountTo = accountFrom;
        }
        accounts.add(accountFrom);
        accounts.addAll(UsersManager.loggedUser.getAccounts());


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        springIndicator = (SpringIndicator) findViewById(R.id.indicator);
        springIndicator.setViewPager(mPager);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    @Override
    public String getTransactionType() {
        return transactionsType;
    }

    @Override
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    @Override
    public void setTransactionDate(Date date) {
        this.transactionDate = date;

    }

    @Override
    public void registerFragment(Fragment fragment) {
        fragments = new ArrayList<>();
        fragments.add(fragment);
    }



    @Override
    public void setCategory(Category category) {
        this.category = category;

    }

    @Override
    public void setAccountFrom(Account account) {
        this.accountFrom = account;
       }

    @Override
    public void setAccountTo(Account account) {
        this.accountTo = account;
      }

    @Override
    public void setAmount(String amount) {
        if (!amount.isEmpty() && !amount.contains(".") && !amount.equals("-")) {
            this.amount = Double.valueOf(amount);
        } else {
            this.amount = 0;
        }
        for (Fragment f: fragments) {
            if (f instanceof SecondPageAddingFragment) {
                ((SecondPageAddingFragment) f).notifyAmountChanged(this.amount);
            }
        }
     }

    @Override
    public double getAmount() {
        Log.e("Amount get", String.valueOf(amount));
        return amount;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    //save transaction
    @Override
    public void saveTransaction() {
        Transaction.TRANSACTIONS_TYPE tp;
        if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {
            tp = Transaction.TRANSACTIONS_TYPE.Transfer;
        } else if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
            tp = Transaction.TRANSACTIONS_TYPE.Income;
        } else {
            tp = Transaction.TRANSACTIONS_TYPE.Expense;
        }
        Transaction transaction = new Transaction(transactionDate.toString(), description, tp, amount, category );
        DBManager.getInstance(this).addTransaction(transactionDate.toString(), description, tp, amount, category );
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void cancelTransaction() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstPageAddingFragment();
                case 1:
                    return new SecondPageAddingFragment();
            }
            return new TransactionsListFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

}
