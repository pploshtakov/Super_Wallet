package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.UsersManager;

public class AccountModifierActivity extends AppCompatActivity {

	private Button cancelButton;
	private Button addButton;

	private EditText accountNameET;
	private EditText accountDescriptionET;
	private RadioButton cashButton;
	private RadioButton accountButton;
	private RadioButton cardButton;

	private int accountId;
	private String accountName;
	private String accountDescription;
	private Account.ACCOUNT_TYPE accountType;

	// TODO create validation for fields

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_modifier);

		cancelButton = (Button) findViewById(R.id.accountAdd_cancel_button);
		addButton = (Button) findViewById(R.id.accountAdd_add_button);

		cashButton = (RadioButton) findViewById(R.id.accountAdd_radio_cash);
		accountButton = (RadioButton) findViewById(R.id.accountAdd_radio_account);
		cardButton = (RadioButton) findViewById(R.id.accountAdd_radio_card);

		accountNameET = (EditText) findViewById(R.id.accountAdd_account_name_ET);
		accountDescriptionET = (EditText) findViewById(R.id.accountAdd_account_description_ET);

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});

		Intent intent = getIntent();
		accountId = -999;
		if (intent != null) {
			accountId = intent.getIntExtra("accountId", -999);
			Log.e("SuperWallet ", "AccountID: " + accountId);
			accountName = intent.getStringExtra("accountName");
			accountDescription = intent.getStringExtra("accountDescription");
			String accountTypeText = intent.getStringExtra("accountType");
			if (accountTypeText != null) {
				if (accountTypeText.equals(Account.ACCOUNT_TYPE.CASH.toString()) || accountTypeText.equals(Account.ACCOUNT_TYPE.ACCOUNT.toString()) || accountTypeText.equals(Account.ACCOUNT_TYPE.CARD.toString())) {
					accountType = Account.ACCOUNT_TYPE.valueOf(accountTypeText);
				}
				else {
					accountType = Account.ACCOUNT_TYPE.CASH;
				}
			}
			else {
				accountType = Account.ACCOUNT_TYPE.CASH;
			}
		}

		// We dont have a category passed in, get first empty index
		if (accountId == -999) {
			accountId = DBManager.getInstance(this).getNextAccountIndex();
			addButton.setText("Add");
		}
		else {
			addButton.setText("Apply");
		}

		if (accountName == null) {
			accountName = "";
		}
		if (accountDescription == null) {
			accountDescription = "";
		}
		if (accountType == null) {
			accountType = Account.ACCOUNT_TYPE.CASH;
		}

		accountNameET.setText(accountName);
		accountDescriptionET.setText(accountDescription);

		switch (accountType) {
			case CASH:
				cashButton.setChecked(true);
				break;
			case ACCOUNT:
				accountButton.setChecked(true);
				break;
			case CARD:
				cardButton.setChecked(true);
				break;
		}

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				accountName = accountNameET.getText().toString();

				accountDescription = accountDescriptionET.getText().toString();
				if (accountName == null || accountName.isEmpty()) {
					accountNameET.setError("Please set a name!");
					accountNameET.requestFocus();
					return;
				}
				if (UsersManager.loggedUser.accountNameExist(accountName)) {
					accountNameET.setError("You has account with same name! Please set description!");
					accountDescriptionET.requestFocus();
					return;
				}
				Intent resultIntent = new Intent();
				resultIntent.putExtra("accountId",accountId);
				resultIntent.putExtra("accountDescription",accountDescription);
				resultIntent.putExtra("accountName",accountName);

				if (cashButton.isChecked()) {
					resultIntent.putExtra("accountType", Account.ACCOUNT_TYPE.CASH.toString());
				}
				else if (accountButton.isChecked()) {
					resultIntent.putExtra("accountType", Account.ACCOUNT_TYPE.ACCOUNT.toString());
				}
				else {
					resultIntent.putExtra("accountType", Account.ACCOUNT_TYPE.CARD.toString());
				}

				setResult(Activity.RESULT_OK, resultIntent);
				finish();
			}
		});
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
	}

}
