package com.example.pesho.superwallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.pesho.superwallet.interfaces.AddTransactionsCommunicator;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.Transfer;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class AddTransactionsActivity extends FragmentActivity implements AddTransactionsCommunicator {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;
    public static final int RESULT_UPDATE_TRANSACTIN_LIST = 11;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ScrollerViewPager mPager;
    SpringIndicator springIndicator;
    String transactionsType;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    ArrayList<Fragment> fragments;
    ArrayList<Account> accounts;
    Transaction transaction;
    private Category category;
    private Account accountFrom;
    private Account accountTo;
    private double amount;
    private String description;
    private int selectedAccountFrom = -1;
    private int selectedAccountTo = -1;
    private LocalDateTime transactionDate;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transactions);

        fragments = new ArrayList<>();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ScrollerViewPager) findViewById(R.id.pager);
        springIndicator = (SpringIndicator) findViewById(R.id.indicator);

        PagerModelManager manager = new PagerModelManager();
        fragments.add(new FirstPageAddingFragment());
        fragments.add(new SecondPageAddingFragment());
        manager.addCommonFragment(fragments, getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        mPager.setAdapter(adapter);
        mPager.fixScrollSpeed();
        springIndicator.setViewPager(mPager);
        //get transaction's type
        Intent intent = getIntent();
        //load accounts
        accounts = new ArrayList<>();
        accountFrom = UsersManager.loggedUser.getDefaultAccount();
        if (transactionsType != null && transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {
            accountTo = accountFrom;
        }
        accounts.add(accountFrom);
        accounts.addAll(UsersManager.loggedUser.getAccounts());

        if (intent.hasExtra("Transaction")) {
            transactionsType = intent.getStringExtra("Transaction");
        }
        if (intent.hasExtra("showInfoFor")) {
            transaction = UsersManager.loggedUser.getTransactionById(intent.getIntExtra("showInfoFor", -1));
            this.transactionsType = transaction.getTransactionType().toString();
            this.category = transaction.getCategory();
            this.accountFrom = transaction.getAccountFrom();
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getAccountId() == accountFrom.getAccountId()) {
                    selectedAccountFrom = i;
                    break;
                }
            }
            if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {
                this.accountTo = ((Transfer)transaction).getAccountTo();
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).getAccountId() == accountTo.getAccountId()) {
                        selectedAccountTo = i;
                        break;
                    }
                }
            }
            this.amount = transaction.getAmount();
            this.description = transaction.getDescription();
            this.transactionDate = transaction.getDate();
            this.location = transaction.getLocation();
        }






        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment;
                if (position == 0) {

                } else {

//                    fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_second_page);
//                    ((SecondPageAddingFragment)fragment).refreshAccountFrom(selectedAccountFrom);
//                    ((SecondPageAddingFragment)fragment).refreshAccountTo(selectedAccountTo);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
    public void setTransactionDate(LocalDateTime date) {
        this.transactionDate = date;

    }

    @Override
    public void registerFragment(Fragment fragment) {
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
        if (!amount.isEmpty() && !amount.equals(".") && !amount.equals("-")) {
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
		Log.e("SuperWallet ", "Save transaction entered.");
        Transaction.TRANSACTIONS_TYPE tp;

		int transactionId = DBManager.getInstance(this).getNextTransactionIndex();
        if (amount <= 0) {
            Toast.makeText(this, R.string.negative_amount_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString()) && amount > accountFrom.getAccountBalance()) {
            Toast.makeText(this, "Not enough money in account!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {

			Transfer transfer = new Transfer(transactionId, transactionDate, amount, accountFrom, accountTo);
			transfer.setDescription(description);
            accountFrom.setBalance(accountFrom.getAccountBalance() - amount);
            accountTo.setBalance(accountTo.getAccountBalance() + amount);
            if (location != null) {
                transfer.setLocation(location);
            }
            UsersManager.loggedUser.addTransaction(transfer);
            DBManager.getInstance(this).addTransaction(transfer);

        } else if (transactionsType.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
            tp = Transaction.TRANSACTIONS_TYPE.valueOf(transactionsType);
			Transaction transaction = new Transaction(transactionId, transactionDate, tp, amount, accountFrom );
			transaction.setCategory(category);
			transaction.setDescription(description);
            accountFrom.setBalance(accountFrom.getAccountBalance() + amount);
            if (location != null) {
                transaction.setLocation(location);
            }
            UsersManager.loggedUser.addTransaction(transaction);
            DBManager.getInstance(this).addTransaction(transaction);
        } else {
            tp = Transaction.TRANSACTIONS_TYPE.valueOf(transactionsType);
            Transaction transaction = new Transaction(transactionId, transactionDate, tp, amount , accountFrom);
            transaction.setCategory(category);
            transaction.setDescription(description);
            accountFrom.setBalance(accountFrom.getAccountBalance() - amount);
            if (location != null) {
                transaction.setLocation(location);
            }
            UsersManager.loggedUser.addTransaction(transaction);
            DBManager.getInstance(this).addTransaction(transaction);
        }

        setResult(RESULT_UPDATE_TRANSACTIN_LIST);
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
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

    }

    public void setSelectedAccountFrom(int selectedAccountFrom) {
        this.selectedAccountFrom = selectedAccountFrom;
        for (Fragment fr: fragments) {
            if (fr instanceof FirstPageAddingFragment) {
                ((FirstPageAddingFragment)fr).refreshAccountFrom(selectedAccountFrom);

            } else {
                ((SecondPageAddingFragment)fr).refreshAccountFrom(selectedAccountFrom);

            }
        }
    }

    public int getSelectedAccountFrom() {
        return selectedAccountFrom;
    }

    public void setSelectedAccountTo(int selectedAccountTo) {
        this.selectedAccountTo = selectedAccountTo;
        for (Fragment fr: fragments) {
            if (fr instanceof FirstPageAddingFragment) {
                ((FirstPageAddingFragment)fr).refreshAccountTo(selectedAccountTo);
            } else {
                ((SecondPageAddingFragment)fr).refreshAccountTo(selectedAccountTo);

            }
        }
    }

    public void backToFirstPage() {
        mPager.setCurrentItem(0);
    }

    public int getSelectedAccountTo() {
        return selectedAccountTo;
    }

    @Override

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void changeTransaction() {
        transaction.setLocation(location);
        transaction.setDate(transactionDate);
        transaction.setDescription(description);
        transaction.setAccountFrom(accountFrom);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        if (transaction instanceof Transfer) {
            DBManager.getInstance(this).updateTransfer(transaction.getTransactionId());
        } else {
            DBManager.getInstance(this).updateTransaction(transaction.getTransactionId());
        }
        setResult(RESULT_UPDATE_TRANSACTIN_LIST);
        finish();
    }

    @Override
    public void updateTransaction(final SecondPageAddingFragment.UpdateTransaction action) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (action.equals(SecondPageAddingFragment.UpdateTransaction.Delete)) {
                            DBManager.getInstance(AddTransactionsActivity.this).deleteTransaction(transaction);
                            UsersManager.loggedUser.deleteTransaction(transaction);
                            setResult(RESULT_UPDATE_TRANSACTIN_LIST);
                            finish();
                        } else {
                            changeTransaction();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        return;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddTransactionsActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //springindicator titles
    private ArrayList<String> getTitles(){
        ArrayList<String> titles = new ArrayList<>();
        titles.add("1");
        titles.add("2");
        return titles;
    }
}
