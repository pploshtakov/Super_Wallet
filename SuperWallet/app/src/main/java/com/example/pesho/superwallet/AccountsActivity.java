package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.UsersManager;

import java.util.ArrayList;

public class AccountsActivity
		extends AppCompatActivity
		implements View.OnDragListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	private static final int ADD_ACCOUNT_REQUEST = 1;
	private static final int MODIFY_ACCOUNT_REQUEST = 2;

	ArrayList<Account> accounts;

	boolean pickingAccount = false;

	private BaseAdapter adapter;
	private int draggedIndex = -1;
	private GridView gridView;

	private Button addAccountButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		accounts = new ArrayList<>();
		gridView = (GridView) findViewById(R.id.accounts_grid_view);
		addAccountButton = (Button) findViewById(R.id.add_account_button);

		adapter = new BaseAdapter() {

			@Override
			// Get a View that displays the data at the specified position in
			// the data set.
			public View getView(int position, View convertView,
								ViewGroup gridView) {
				// try to reuse the views.
				TextView view = (TextView) convertView;
				// if convert view is null then create a new instance else reuse
				// it
				if (view == null) {
					view = new TextView(AccountsActivity.this);
				}
				Log.e("SuperWallet ", "Accounts size: " + accounts.size());
				Account acc = accounts.get(position);
				String accName = acc.getAccountName();
				view.setText(accName);
				view.setTag(accounts.get(position).getAccountId());
				return view;
			}

			@Override
			// Get the row id associated with the specified position in the
			// list.
			public long getItemId(int position) {
				return position;
			}

			@Override
			// Get the data item associated with the specified position in the
			// data set.
			public Object getItem(int position) {
				return accounts.get(position);
			}

			@Override
			// How many items are in the data set represented by this Adapter.
			public int getCount() {
				return accounts.size();
			}
		};

		gridView.setAdapter(adapter);
		// Set on Item Click (boolean pickingCategory determines the behavior)
		gridView.setOnItemClickListener(AccountsActivity.this);

		// Check the intent if we're just picking a category
		Intent intent = getIntent();
		if (intent != null) {
			pickingAccount = intent.getBooleanExtra("pickingAccount", false);
		}

		// If we are, add the default categories to the list
		// TODO hide other buttons and trash can, disable dragging
		if (pickingAccount) {
			Account acc = UsersManager.loggedUser.getDefaultAccount();
			Log.e("SuperWallet ", "Account name: " + acc);
			accounts.add(acc);

			addAccountButton.setVisibility(View.GONE);
		}
		// If we're not picking a category, add dragging and trash can and dont add the default categories
		// to the list.
		else {
			addAccountButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AccountsActivity.this, CategoryModifierActivity.class);
					startActivityForResult(intent, ADD_ACCOUNT_REQUEST);
				}
			});

			gridView.setOnItemLongClickListener(AccountsActivity.this);

			addAccountButton.setVisibility(View.VISIBLE);
		}
		accounts.addAll(UsersManager.loggedUser.getAccounts());

	}

	@Override
	public boolean onDrag(View view, DragEvent dragEvent) {
		switch (dragEvent.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Drag has started
				// If called for trash resize the view and return true
				if (view.getId() == R.id.trash_can) {
					view.animate().scaleX(1.0f);
					view.animate().scaleY(1.0f);
					return true;
				} else // else check the mime type and set the view visibility
					if (dragEvent.getClipDescription().hasMimeType(
							ClipDescription.MIMETYPE_TEXT_PLAIN)) {
						view.setVisibility(View.GONE);
						return true;

					} else {
						return false;
					}
			case DragEvent.ACTION_DRAG_ENTERED:
				// Drag has entered view bounds
				// If called for trash can then scale it.
				if (view.getId() == R.id.trash_can) {
					view.animate().scaleX(1.5f);
					view.animate().scaleY(1.5f);
				}
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				// Drag exited view bounds
				// If called for trash can then reset it.
				if (view.getId() == R.id.trash_can) {
					view.animate().scaleX(1.0f);
					view.animate().scaleY(1.0f);
				}
				view.invalidate();
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				// Ignore this event
				return true;
			case DragEvent.ACTION_DROP:
				// Dropped inside view bounds
				// If called for trash can then delete the item and reload the grid
				// view
				if (view.getId() == R.id.trash_can) {
					accounts.remove(draggedIndex);
					draggedIndex = -1;
				}
				adapter.notifyDataSetChanged();
			case DragEvent.ACTION_DRAG_ENDED:
				// Hide the trash can
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						findViewById(R.id.trash_can).setVisibility(View.GONE);
					}
				}, 1000L);
				if (view.getId() == R.id.trash_can) {
					view.animate().scaleX(1.0f);
					view.animate().scaleY(1.0f);
				} else {
					view.setVisibility(View.VISIBLE);
				}
				// remove drag listeners
				view.setOnDragListener(null);
				return true;

		}
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView gridView, View view,
								   int position, long row) {
		ClipData.Item item = new ClipData.Item((String) view.getTag());
		ClipData clipData = new ClipData((CharSequence) view.getTag(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		view.startDrag(clipData, new View.DragShadowBuilder(view), null, 0);
		View trashCan = findViewById(R.id.trash_can);
		trashCan.setVisibility(View.VISIBLE);
		trashCan.setOnDragListener(AccountsActivity.this);

		trashCan.setOnDragListener(AccountsActivity.this);
		draggedIndex = position;
		return true;
	}

	@Override
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		Intent intent;

		if (!pickingAccount) {
			intent = new Intent(AccountsActivity.this, CategoryModifierActivity.class);
//			intent.putExtra("categoryId", accounts.get(position).getCategoryId());
//			intent.putExtra("categoryName", categories.get(position).getCategoryName());
//			intent.putExtra("categoryDescription", categories.get(position).getCategoryDescription());
//			intent.putExtra("categoryIcon", categories.get(position).getCategoryIcon());
//			intent.putExtra("categoryType", categories.get(position).getTransactionType().toString());

			startActivityForResult(intent, MODIFY_ACCOUNT_REQUEST);
		}
		else {
			intent = new Intent();
			intent.putExtra("accountId", accounts.get(position).getAccountId());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}


	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stats:
                Toast.makeText(AccountsActivity.this, "stats", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                finish();
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
                startActivity(accounts);
                return true;
            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(AccountsActivity.this, "settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
        MenuItem itemView = menu.findItem(R.id.action_accounts);
        itemView.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

}
