package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

	private Button cancelButton;
	private Button addButton;

	EditText categoryNameET;
	EditText categoryDescriptionET;
	ImageView categoryIconIV;
	RadioButton incomeButton;
	RadioButton expenseButton;

	boolean isIncome = false;

	Category category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_modifier);

		cancelButton = (Button) findViewById(R.id.categoryAdd_cancel_button);
		addButton = (Button) findViewById(R.id.categoryAdd_add_button);
		incomeButton = (RadioButton) findViewById(R.id.categoryAdd_radio_income);
		expenseButton = (RadioButton) findViewById(R.id.categoryAdd_radio_expense);

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});

		Intent intent = getIntent();
		int categoryId = -999;
		if (intent != null) {
			categoryId = intent.getIntExtra("categoryId", -999);
		}

		// We dont have a category passed in, get first empty index
		if (categoryId == -999) {
			categoryId = DBManager.getInstance(this).getNextUserCategoryIndex();
			category = new Category(categoryId, Transaction.TRANSACTIONS_TYPE.Expense, "", R.drawable.empty_icon);
		}
		else {
			category = UsersManager.loggedUser.getCategory(categoryId);
			if (category == null) {
				category = new Category(categoryId, Transaction.TRANSACTIONS_TYPE.Expense, "", R.drawable.empty_icon);
			}
		}
		categoryNameET.setText(category.getCategoryName());
		categoryDescriptionET.setText(category.getCategoryDescription());

		categoryIconIV.setImageResource(category.getCategoryIcon());
		categoryIconIV.setTag(category.getCategoryIcon());

		switch (category.getTransactionType()) {
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
				category.setCategoryDescription(categoryDescriptionET.getText().toString());
				category.setCategoryName(categoryNameET.getText().toString());
				category.setCategoryIcon((Integer)categoryIconIV.getTag());

				if (expenseButton.isChecked()) {
					category.setCategoryType(Transaction.TRANSACTIONS_TYPE.Expense);
				}
				else {
					category.setCategoryType(Transaction.TRANSACTIONS_TYPE.Income);
				}

				setResult(Activity.RESULT_OK);
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
}
