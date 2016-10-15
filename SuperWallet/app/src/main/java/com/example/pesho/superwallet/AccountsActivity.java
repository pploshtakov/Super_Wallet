package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.UsersManager;

import java.util.ArrayList;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
public class AccountsActivity
		extends AppCompatActivity
		implements View.OnDragListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	private static final int ADD_ACCOUNT_REQUEST = 1;
	private static final int MODIFY_ACCOUNT_REQUEST = 2;
	private BoomMenuButton boomMenuButtonInActionBar;
	ArrayList<Account> accounts;

	boolean pickingAccount = false;

	private BaseAdapter adapter;
	private int draggedIndex = -1;
	private ListView listView;
	private boolean init = false;
	private Button addAccountButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

		View mCustomView;
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		mTitleTextView.setText(R.string.accounts);
		boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);




		accounts = new ArrayList<Account>();
		listView = (ListView) findViewById(R.id.accounts_list_view);
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
				view.setText(accounts.get(position).getAccountName());
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

		listView.setAdapter(adapter);
		// Set on Item Click (boolean pickingAccount determines the behavior)
		listView.setOnItemClickListener(AccountsActivity.this);

		// Check the intent if we're just picking an accont
		Intent intent = getIntent();
		if (intent != null) {
			pickingAccount = intent.getBooleanExtra("pickingAccount", false);
		}

		// If we are, add the default account to the list
		// TODO hide other buttons and trash can, disable dragging
		if (pickingAccount) {
			accounts.add(UsersManager.loggedUser.getDefaultAccount());

			addAccountButton.setVisibility(View.GONE);
		}
		// If we're not picking a account, add dragging and trash can and dont add the default account
		// to the list.
		else {
			addAccountButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AccountsActivity.this, AccountModifierActivity.class);
					startActivityForResult(intent, ADD_ACCOUNT_REQUEST);
				}
			});

			listView.setOnItemLongClickListener(AccountsActivity.this);

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
				if (view.getId() == R.id.accounts_trash_can) {
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
				if (view.getId() == R.id.accounts_trash_can) {
					view.animate().scaleX(1.5f);
					view.animate().scaleY(1.5f);
				}
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				// Drag exited view bounds
				// If called for trash can then reset it.
				if (view.getId() == R.id.accounts_trash_can) {
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
				if (view.getId() == R.id.accounts_trash_can) {
					accounts.remove(draggedIndex);
					draggedIndex = -1;
				}
				adapter.notifyDataSetChanged();
			case DragEvent.ACTION_DRAG_ENDED:
				// Hide the trash can
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						findViewById(R.id.accounts_trash_can).setVisibility(View.GONE);
					}
				}, 1000L);
				if (view.getId() == R.id.accounts_trash_can) {
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
		ClipData.Item item = new ClipData.Item(view.getTag().toString());
		ClipData clipData = new ClipData( view.getTag().toString(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		view.startDrag(clipData, new View.DragShadowBuilder(view), null, 0);
		View trashCan = findViewById(R.id.accounts_trash_can);
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
			intent = new Intent(AccountsActivity.this, AccountModifierActivity.class);
//			intent.putExtra("categoryId", accounts.get(position).getCategoryId());
//			intent.putExtra("categoryName", categories.get(position).getCategoryName());
//			intent.putExtra("categoryDescription", categories.get(position).getCategoryDescription());
//			intent.putExtra("categoryIcon", categories.get(position).getCategoryIcon());
//			intent.putExtra("categoryType", categories.get(position).getTransactionType().toString());
			intent.putExtra("accountId", accounts.get(position).getAccountId());
			intent.putExtra("accountName", accounts.get(position).getAccountName());
			intent.putExtra("accountDescription", accounts.get(position).getAccountDescription());
			intent.putExtra("accountType", accounts.get(position).getAccountType().toString());
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
//            case R.id.action_home:
//                Intent home = new Intent(this, MainActivity.class);
//                startActivity(home);
//                finish();
//                return true;
//            case R.id.action_category:
//                // User chose the "Settings" item, show the app settings UI...
//                Intent category = new Intent(this, CategoryListActivity.class);
//                startActivity(category);
//                return true;
//            case R.id.action_accounts:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Intent accounts = new Intent(this, AccountsActivity.class);
//                startActivity(accounts);
//                return true;
//            case R.id.action_settings:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Toast.makeText(AccountsActivity.this, "settings", Toast.LENGTH_SHORT).show();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_actions, menu);
//        MenuItem itemView = menu.findItem(R.id.action_accounts);
//        itemView.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (data != null) {
				if (requestCode == ADD_ACCOUNT_REQUEST) {
					int accountId = data.getIntExtra("accountId", -999);
					String accountName = data.getStringExtra("accountName");
					String accountDescription = data.getStringExtra("accountDescription");
					Account.ACCOUNT_TYPE accountType = Account.ACCOUNT_TYPE.valueOf(data.getStringExtra("accountType"));
					Account account = new Account(accountId, accountName, 0.0, accountType);
					account.setAccountDescription(accountDescription);

					accounts.add(account);
					UsersManager.loggedUser.addAccount(account);
					adapter.notifyDataSetChanged();

					DBManager.getInstance(this).addAccount(account);
				}

				if (requestCode == MODIFY_ACCOUNT_REQUEST) {
					int accountId = data.getIntExtra("accountId", -999);
					Account account = UsersManager.loggedUser.getAccount(accountId);
					if (account != null) {
						account.setAccountName(data.getStringExtra("accountName"));
						account.setAccountDescription(data.getStringExtra("accountDescription"));
						account.setAccountType(Account.ACCOUNT_TYPE.valueOf(data.getStringExtra("accountType")));
						adapter.notifyDataSetChanged();

						DBManager.getInstance(this).updateAccount(account);
					}
				}
			}
		}
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
					R.drawable.fridge,
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
					.addSubButton(ContextCompat.getDrawable(this, R.drawable.house), subButtonColors[0], "BoomMenuButton")
					.addSubButton(ContextCompat.getDrawable(this, R.drawable.category), subButtonColors[0], "View source code")
					.addSubButton(ContextCompat.getDrawable(this, R.drawable.settings), subButtonColors[0], "Follow me")
					.button(ButtonType.HAM)
					.boom(BoomType.PARABOLA)
					.place(PlaceType.HAM_3_1)
					.subButtonTextColor(ContextCompat.getColor(this, R.color.black))
					.subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
					.delay(50)
					.duration(600)
					.showOrder(OrderType.REVERSE)
					.init(boomMenuButtonInActionBar).setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener() {

				@Override
				public void onClick(int buttonIndex) {
					Log.e("SuperWallet ", "Button index: " + buttonIndex);
					if (buttonIndex == 0) {
						Intent intent = new Intent(AccountsActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
					if (buttonIndex == 1) {
						Intent intent = new Intent(AccountsActivity.this, CategoryListActivity.class);
						startActivity(intent);
						finish();
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
