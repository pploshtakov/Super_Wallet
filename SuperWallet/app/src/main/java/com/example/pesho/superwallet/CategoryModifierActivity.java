package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

public class CategoryModifierActivity extends AppCompatActivity {

	private static final int CHOOSE_ICON = 1;

	private Button cancelButton;
	private Button addButton;

	private EditText categoryNameET;
	private EditText categoryDescriptionET;
	private ImageView categoryIconIV;
	private RadioButton incomeButton;
	private RadioButton expenseButton;

	private boolean isIncome = false;

	private int categoryId;
	private String categoryName;
	private String categoryDescription;
	private int categoryIcon;
	private Transaction.TRANSACTIONS_TYPE transactionType;

	// TODO create validation for fields

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_modifier);

		cancelButton = (Button) findViewById(R.id.categoryAdd_cancel_button);
		addButton = (Button) findViewById(R.id.categoryAdd_add_button);

		incomeButton = (RadioButton) findViewById(R.id.categoryAdd_radio_income);
		expenseButton = (RadioButton) findViewById(R.id.categoryAdd_radio_expense);

		categoryNameET = (EditText) findViewById(R.id.categoryAdd_category_name_ET);
		categoryDescriptionET = (EditText) findViewById(R.id.categoryAdd_category_description_ET);
		categoryIconIV = (ImageView) findViewById(R.id.categoryAdd_category_icon_IV);

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});

		Intent intent = getIntent();
		categoryId = -999;
		if (intent != null) {
			categoryId = intent.getIntExtra("categoryId", -999);
			categoryName = intent.getStringExtra("categoryName");
			categoryDescription = intent.getStringExtra("categoryDescription");
			categoryIcon = intent.getIntExtra("categoryIcon", R.drawable.empty_icon);
			String transactionTypeText = intent.getStringExtra("categoryType");
			if (transactionTypeText != null) {
				if (transactionTypeText.equals(Transaction.TRANSACTIONS_TYPE.Expense.toString()) || transactionTypeText.equals(Transaction.TRANSACTIONS_TYPE.Income.toString())) {
					transactionType = Transaction.TRANSACTIONS_TYPE.valueOf(transactionTypeText);
				}
				else {
					transactionType = Transaction.TRANSACTIONS_TYPE.Expense;
				}
			}
			else {
				transactionType = Transaction.TRANSACTIONS_TYPE.Expense;
			}
		}

		// We dont have a category passed in, get first empty index
		if (categoryId == -999) {
			categoryId = DBManager.getInstance(this).getNextUserCategoryIndex();
			addButton.setText("Add");
		}
		else {
			addButton.setText("Apply");
		}

		if (categoryName == null) {
			categoryName = "";
		}
		if (categoryDescription == null) {
			categoryDescription = "";
		}
		if (transactionType == null) {
			transactionType = Transaction.TRANSACTIONS_TYPE.Expense;
		}

		categoryNameET.setText(categoryName);
		categoryDescriptionET.setText(categoryDescription);

		categoryIconIV.setImageResource(categoryIcon);
		categoryIconIV.setTag(categoryIcon);

		categoryIconIV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CategoryModifierActivity.this, CategoryIconChooserActivity.class);
				startActivityForResult(intent, CHOOSE_ICON);
			}
		});

		switch (transactionType) {
			case Income:
				incomeButton.setChecked(true);
				break;
			case Expense:
				expenseButton.setChecked(true);
				break;
		}

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				categoryName = categoryNameET.getText().toString();

				categoryDescription = categoryDescriptionET.getText().toString();

				if (categoryIconIV.getTag() != null) {
					categoryIcon = (Integer) categoryIconIV.getTag();
				} else {
					categoryIcon = R.drawable.empty_icon;
				}

				Intent resultIntent = new Intent();
				resultIntent.putExtra("categoryId",categoryId);
				resultIntent.putExtra("categoryDescription",categoryDescription);
				resultIntent.putExtra("categoryName",categoryName);
				resultIntent.putExtra("categoryIcon",categoryIcon);

				if (expenseButton.isChecked()) {
					resultIntent.putExtra("categoryType",Transaction.TRANSACTIONS_TYPE.Expense.toString());
				}
				else {
					resultIntent.putExtra("categoryType",Transaction.TRANSACTIONS_TYPE.Income.toString());
				}

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
			case R.id.categoryAdd_radio_income:
				if (checked) {
					isIncome = true;
				}
				break;
			case R.id.categoryAdd_radio_expense:
				if (checked) {
					isIncome = false;
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CHOOSE_ICON) {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					int iconResource = data.getIntExtra("iconResource", R.drawable.empty_icon);
					categoryIconIV.setImageResource(iconResource);
					categoryIconIV.setTag(iconResource);
					categoryIcon = iconResource;
				}
			}
		}
	}
}
