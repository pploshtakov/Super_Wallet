package com.example.pesho.superwallet;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesho.superwallet.interfaces.AddTransactionsCommunicator;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;
import com.example.pesho.superwallet.myViews.AutoResizeTextView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstPageAddingFragment extends Fragment {
    public static final int PICK_CATEGORY = 1;

    public void refreshAccountFrom(int selectedAccountFrom) {
        if (selectedAccountFrom != -1)
        accountsSpinner.setSelection(selectedAccountFrom);
    }

    public void refreshAccountTo(int selectedAccountTo) {
        if (selectedAccountTo != -1)
        accountsSpinner1.setSelection(selectedAccountTo);
    }

    enum ARITHMETIC_SIGN {PLUS, MINUS, DIVIDE, MUL}
    Category category;
    ArrayList<Category> defaultCategories;
    AddTransactionsCommunicator myActivity;
    //subScore from operations
    String subScore;
    //last typed value
    String lastValue;
    ARITHMETIC_SIGN lastAction;
    Button zero;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;
    Button six;
    Button seven;
    Button eight;
    Button nine;
    ImageButton backspace;
    Button plus;
    Button minus;
    Button divide;
    Button mul;
    Button result;
    Button comma;
    AutoResizeTextView amountTV;
    TextView transactionTypeTV;
    Button selectCategoryButton;
    Spinner accountsSpinner;
    Spinner accountsSpinner1;
    ImageButton saveButton;
    ImageButton cancelButton;
    Transaction transaction;
    ArrayAdapter adapter;
    public FirstPageAddingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first_page_adding, container, false);

        myActivity.registerFragment(this);
        //save transaction
        saveButton = (ImageButton) root.findViewById(R.id.addt_first_page_save_button);

        transaction = UsersManager.loggedUser.getTransactionById(getActivity().getIntent().getIntExtra("showInfoFor", -1));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getIntent().hasExtra("showInfoFor")) {
                    myActivity.updateTransaction(SecondPageAddingFragment.UpdateTransaction.Update);
                } else {
                    myActivity.saveTransaction();
                }
            }
        });



        //cancel transaction
        cancelButton = (ImageButton) root.findViewById(R.id.addt_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.cancelTransaction();
            }
        });
        //set transaction type TV
        transactionTypeTV = (TextView) root.findViewById(R.id.addt_transaction_type_TV);
        if (getActivity().getIntent().hasExtra("showInfoFor")) {
            transactionTypeTV.setText(transaction.getTransactionType().toString());
        } else {
            transactionTypeTV.setText(myActivity.getTransactionType());
        }
        //accounts spinner
        accountsSpinner = (Spinner) root.findViewById(R.id.addt_account_spinner);
        adapter = new ArrayAdapter((Context) myActivity, R.layout.spinner_layout, myActivity.getAccounts());
        accountsSpinner.setAdapter(adapter);
        if (myActivity.getSelectedAccountFrom() != -1) {
            accountsSpinner.setSelection(myActivity.getSelectedAccountFrom());
        }

        //spinners listener
        accountsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = myActivity.getAccounts().get(position);
                myActivity.setAccountFrom(selectedAccount);
                myActivity.setSelectedAccountFrom(accountsSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //select category button

        selectCategoryButton = (Button) root.findViewById(R.id.addt_select_category_button);


        if (myActivity.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Expense.toString()) ||
                myActivity.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
            if (transaction != null) {
                category = transaction.getCategory();
            } else {
                //!!!!!!!!!!!!!!!! get first default category from list not from DB!!!!!
                defaultCategories = UsersManager.getInstance(getContext()).getDefaultCategories();
                category = defaultCategories.get(0);
                myActivity.setCategory(category);
            }
            selectCategoryButton.setText(category.getCategoryName());
            selectCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent category2 = new Intent((Context) myActivity, CategoryListActivity.class);
                    category2.putExtra("pickingCategory", true);
                    category2.putExtra("categoryType", myActivity.getTransactionType());
                    startActivityForResult(category2, PICK_CATEGORY);
                }
            });

        } else {
            selectCategoryButton.setVisibility(View.GONE);
            accountsSpinner1 = (Spinner) root.findViewById(R.id.addt_account1_spinner);
            accountsSpinner1.setVisibility(View.VISIBLE);
            if (myActivity.getSelectedAccountTo() != -1) {
                accountsSpinner1.setSelection(myActivity.getSelectedAccountTo());
            }
            accountsSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Account selectedAccount = myActivity.getAccounts().get(position);
                    myActivity.setAccountTo(selectedAccount);
                    myActivity.setSelectedAccountTo(accountsSpinner1.getSelectedItemPosition());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //accounts spinner 1
            accountsSpinner1.setAdapter(adapter);
        }



        //add calculator buttons
        amountTV = (AutoResizeTextView) root.findViewById(R.id.addt_amount_tv);
        if (transaction != null) {
            amountTV.setText(String.valueOf(transaction.getAmount()));
        }
        zero = (Button) root.findViewById(R.id.button_zero);
        one = (Button) root.findViewById(R.id.button_one);
        two = (Button) root.findViewById(R.id.button_two);
        three = (Button) root.findViewById(R.id.button_three);
        four = (Button) root.findViewById(R.id.button_four);
        five = (Button) root.findViewById(R.id.button_five);
        six = (Button) root.findViewById(R.id.button_six);
        seven = (Button) root.findViewById(R.id.button_seven);
        eight = (Button) root.findViewById(R.id.button_eight);
        nine = (Button) root.findViewById(R.id.button_nine);
        backspace = (ImageButton) root.findViewById(R.id.button_backspace);
        plus = (Button) root.findViewById(R.id.button_plus);
        minus = (Button) root.findViewById(R.id.button_minus);
        divide = (Button) root.findViewById(R.id.button_divide);
        mul = (Button) root.findViewById(R.id.button_mul);
        result = (Button) root.findViewById(R.id.button_result);
        comma = (Button) root.findViewById(R.id.button_comma);
        //listener for arithmetic operation buttons
        View.OnClickListener aritmListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subScore == null) {
                    subScore = amountTV.getText().toString();
                    if (subScore.isEmpty()) {
                        subScore = "0";
                    }
                    int buttonClicked = v.getId();
                    switch (buttonClicked) {
                        case R.id.button_plus:
                            lastAction = ARITHMETIC_SIGN.PLUS;
                            break;
                        case R.id.button_minus:
                            lastAction = ARITHMETIC_SIGN.MINUS;
                            break;
                        case R.id.button_divide:
                            lastAction = ARITHMETIC_SIGN.DIVIDE;
                            break;
                        case R.id.button_mul:
                            lastAction = ARITHMETIC_SIGN.MUL;
                            break;
                    }
                } else {

                    subScore = arithmeticOperation(Double.valueOf(amountTV.getText().toString()), lastAction);

                    int buttonClicked = v.getId();
                    switch (buttonClicked) {
                        case R.id.button_plus:
                            lastAction = ARITHMETIC_SIGN.PLUS;
                            break;
                        case R.id.button_minus:
                            lastAction = ARITHMETIC_SIGN.MINUS;
                            break;
                        case R.id.button_divide:
                            lastAction = ARITHMETIC_SIGN.DIVIDE;
                            break;
                        case R.id.button_mul:
                            lastAction = ARITHMETIC_SIGN.MUL;
                            break;
                        case R.id.button_result:
                            amountTV.setText(subScore);
                            myActivity.setAmount(subScore);
                            subScore = null;
                            lastAction = null;
                            return;
                    }
                    amountTV.setText(subScore);
                    myActivity.setAmount(subScore);
                }
                lastValue = "";
            }
        };
        //digit, ',' and backspace listener
        View.OnClickListener digitListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int buttonClicked = v.getId();
                char symbol = '0';
                if (lastValue == null || !lastValue.isEmpty()) {
                    lastValue = amountTV.getText().toString();
                }

                switch (buttonClicked) {
                    case R.id.button_zero:
                        break;
                    case R.id.button_one:
                        symbol = '1';
                        break;
                    case R.id.button_two:
                        symbol = '2';
                        break;
                    case R.id.button_three:
                        symbol = '3';
                        break;
                    case R.id.button_four:
                        symbol = '4';
                        break;
                    case R.id.button_five:
                        symbol = '5';
                        break;
                    case R.id.button_six:
                        symbol = '6';
                        break;
                    case R.id.button_seven:
                        symbol = '7';
                        break;
                    case R.id.button_eight:
                        symbol = '8';
                        break;
                    case R.id.button_nine:
                        symbol = '9';
                        break;
                    case R.id.button_comma:
                        symbol = '.';
                        break;
                    case R.id.button_backspace:
                        if (amountTV.getText().toString().length() > 0) {
                            amountTV.setText(amountTV.getText().toString().substring(0, amountTV.getText().toString().length() - 1));
                            myActivity.setAmount(amountTV.getText().toString());
                        }
                        return;
                }
                if (lastValue.equals("0")) {

                    if (symbol == '.') {
                        amountTV.setText("0.");
                    } else {
                        amountTV.setText(symbol + "");
                    }

                    amountTV.setText(Character.toString(symbol));

                } else {
                    if(!lastValue.isEmpty()) {
                        amountTV.setText(lastValue + symbol);
                    }else {
                        if (symbol == '.') {
                            lastValue = "0";
                        }
                        lastValue += symbol;
                        amountTV.setText(lastValue);
                    }
                }
                Log.e("Amount", amountTV.getText().toString());
                myActivity.setAmount(amountTV.getText().toString());
            }
        };
        //set click listeners
        zero.setOnClickListener(digitListener);
        one.setOnClickListener(digitListener);
        two.setOnClickListener(digitListener);
        three.setOnClickListener(digitListener);
        four.setOnClickListener(digitListener);
        five.setOnClickListener(digitListener);
        six.setOnClickListener(digitListener);
        seven.setOnClickListener(digitListener);
        eight.setOnClickListener(digitListener);
        nine.setOnClickListener(digitListener);
        backspace.setOnClickListener(digitListener);
        comma.setOnClickListener(digitListener);
        plus.setOnClickListener(aritmListener);
        minus.setOnClickListener(aritmListener);
        divide.setOnClickListener(aritmListener);
        mul.setOnClickListener(aritmListener);
        result.setOnClickListener(aritmListener);
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                amountTV.setText("0");
                myActivity.setAmount(amountTV.getText().toString());
                return false;
            }
        });

        return root;


    }
    //arithmetic operations
    private String arithmeticOperation(Double value, ARITHMETIC_SIGN sign) {
        if (sign == null) {
            return String.valueOf(value);
        }
                switch (sign) {
                    case PLUS:
                        return String.valueOf(Double.valueOf(subScore) + value);
                    case MINUS:
                        return String.valueOf(Double.valueOf(subScore) - value);
                    case DIVIDE:
                        return String.valueOf(Double.valueOf(subScore) / value);
                    case MUL:
                        return String.valueOf(Double.valueOf(subScore) * value);

                }
        return subScore;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (AddTransactionsCommunicator) context;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CATEGORY && resultCode == RESULT_OK) {

            if (data.hasExtra("categoryId")) {
                for (int i = 0; i < defaultCategories.size(); i++) {
                    if (defaultCategories.get(i).getCategoryId() == data.getIntExtra("categoryId", 0)) {
                        category = defaultCategories.get(i);
                        myActivity.setCategory(category);
                        break;
                    }
                }
                selectCategoryButton.setText(category.getCategoryName());
            }
        }
    }
}
