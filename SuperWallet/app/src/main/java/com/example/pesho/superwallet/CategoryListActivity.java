package com.example.pesho.superwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.OrderType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

public class CategoryListActivity
	extends AppCompatActivity
	implements OnDragListener, OnItemLongClickListener, OnItemClickListener {

	private static final int ADD_CATEGORY_REQUEST = 1;
	private static final int MODIFY_CATEGORY_REQUEST = 2;
	private BoomMenuButton boomMenuButtonInActionBar;
	ArrayList<Category> categories;

	boolean pickingCategory = false;
	private boolean init = false;
	private BaseAdapter adapter;
	private int draggedIndex = -1;
	private GridView gridView;

	private Button addCategoryButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);

		View mCustomView;
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
		//mTitleTextView.setText(R.string.category);
		boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);

		addCategoryButton = (Button) findViewById(R.id.add_category_button);

		// Check the intent if we're just picking a category
		Intent intent = getIntent();
		String categoryType = null;
		if (intent != null) {
			pickingCategory = intent.getBooleanExtra("pickingCategory", false);
			categoryType = intent.getStringExtra("categoryType");
		}

		// Create the categories array and get the gridView
		categories = new ArrayList<>();
		gridView = (GridView) findViewById(R.id.category_grid_view);

		// If we are, add the default categories to the list
		// TODO hide other buttons and trash can, disable dragging
		if (pickingCategory) {
			ArrayList<Category> defaultCategories = DBManager.getInstance(this).loadDefaultCategories();
			if (categoryType != null) {
				for (Category cat: defaultCategories) {
					if (cat.getTransactionType().toString().equals(categoryType)) {
						categories.add(cat);
						Log.e("SuperWallet ", "CATEGORY cat: " + cat.getCategoryName() );
					}
				}
			}
			else {
				categories.addAll(defaultCategories);
			}

			addCategoryButton.setVisibility(View.GONE);
		}
		// If we're not picking a category, add dragging and trash can and dont add the default categories
		// to the list.
		else {
			addCategoryButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CategoryListActivity.this, CategoryModifierActivity.class);
					startActivityForResult(intent, ADD_CATEGORY_REQUEST);
				}
			});


			gridView.setOnItemLongClickListener(CategoryListActivity.this);

			addCategoryButton.setVisibility(View.VISIBLE);
		}

		ArrayList<Category> userCategories = UsersManager.loggedUser.getCategories();
		if (categoryType != null) {
			for (Category cat: userCategories) {
				if (cat.getTransactionType().toString().equals(categoryType)){
					categories.add(cat);
				}
			}
		}
		else {
			categories.addAll(userCategories);
		}



		adapter = new BaseAdapter() {

			@Override
			// Get a View that displays the data at the specified position in
			// the data set.
			public View getView(int position, View convertView,
								ViewGroup gridView) {
				// try to reuse the views.
				ImageView view = (ImageView) convertView;
				// if convert view is null then create a new instance else reuse
				// it
				if (view == null) {
					view = new ImageView(CategoryListActivity.this);
				}
				view.setImageResource(categories.get(position).getCategoryIcon());
				view.setTag(String.valueOf(position));
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
				return categories.get(position);
			}

			@Override
			// How many items are in the data set represented by this Adapter.
			public int getCount() {
				return categories.size();
			}
		};

		gridView.setAdapter(adapter);
		// Set on Item Click (boolean pickingCategory determines the behavior)
		gridView.setOnItemClickListener(CategoryListActivity.this);
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
					categories.remove(draggedIndex);
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
		// Call requires higher api level
		//view.startDragAndDrop(clipData, new View.DragShadowBuilder(view), null, 0);
		View trashCan = findViewById(R.id.trash_can);
		trashCan.setVisibility(View.VISIBLE);
		trashCan.setOnDragListener(CategoryListActivity.this);

		trashCan.setOnDragListener(CategoryListActivity.this);
		draggedIndex = position;
		return true;
	}

	@Override
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		Intent intent;

		Log.e("SuperWallet", "CatID: " + categories.get(position).getCategoryId() );

		if (!pickingCategory) {
			intent = new Intent(CategoryListActivity.this, CategoryModifierActivity.class);
			intent.putExtra("categoryId", categories.get(position).getCategoryId());
			intent.putExtra("categoryName", categories.get(position).getCategoryName());
			intent.putExtra("categoryDescription", categories.get(position).getCategoryDescription());
			intent.putExtra("categoryIcon", categories.get(position).getCategoryIcon());
			intent.putExtra("categoryType", categories.get(position).getTransactionType().toString());

			startActivityForResult(intent, MODIFY_CATEGORY_REQUEST);
		}
		else {
			intent = new Intent();
			intent.putExtra("categoryId", categories.get(position).getCategoryId());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_stats:
				Intent statisticsIntent = new Intent(this, StatisticsActivity.class);
				startActivity(statisticsIntent);
				return true;
//			case R.id.action_home:
//				Intent home = new Intent(this, MainActivity.class);
//				finish();
//				return true;
//			case R.id.action_category:
//				// User chose the "Settings" item, show the app settings UI...
//				Intent category = new Intent(this, CategoryListActivity.class);
//				startActivity(category);
//				return true;
//			case R.id.action_accounts:
//				// User chose the "Favorite" action, mark the current item
//				// as a favorite...
//				Intent accounts = new Intent(this, AccountsActivity.class);
//				startActivity(accounts);
//				return true;
//			case R.id.action_settings:
//				// User chose the "Favorite" action, mark the current item
//				// as a favorite...
//				Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
//				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_actions, menu);
//		MenuItem itemView = menu.findItem(R.id.action_category);
//		itemView.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (data != null) {
				if (requestCode == ADD_CATEGORY_REQUEST) {
					Log.e("SuperWallet ", "Request code: ADD CATEGORY");
					int categoryId = data.getIntExtra("categoryId", -999);
					String categoryName = data.getStringExtra("categoryName");
					String categoryDescription = data.getStringExtra("categoryDescription");
					int categoryIcon = data.getIntExtra("categoryIcon", R.drawable.empty_icon);
					Transaction.TRANSACTIONS_TYPE categoryType = Transaction.TRANSACTIONS_TYPE.valueOf(data.getStringExtra("categoryType"));
					Category category = new Category(categoryId, categoryType, categoryName, categoryIcon);
					category.setCategoryDescription(categoryDescription);

					categories.add(category);
					UsersManager.loggedUser.addCategory(category);
					adapter.notifyDataSetChanged();

					DBManager.getInstance(this).addCategory(category);

					Log.e("SuperWallet ", "Category ID " + data.getIntExtra("categoryId", -999) );
					Log.e("SuperWallet ", "Category Name " + data.getStringExtra("categoryName") );
					Log.e("SuperWallet ", "Category Description " + data.getStringExtra("categoryDescription") );
					Log.e("SuperWallet ", "Category Icon " + data.getIntExtra("categoryIcon", R.drawable.empty_icon) );
				}

				if (requestCode == MODIFY_CATEGORY_REQUEST) {
					Log.e("SuperWallet ", "Request code: MODIFY CATEGORY");
					int categoryId = data.getIntExtra("categoryId", -999);
					Category category = UsersManager.loggedUser.getCategory(categoryId);
					if (category != null) {
						category.setCategoryName(data.getStringExtra("categoryName"));
						category.setCategoryDescription(data.getStringExtra("categoryDescription"));
						category.setCategoryIcon(data.getIntExtra("categoryIcon", R.drawable.empty_icon));
						category.setCategoryType(Transaction.TRANSACTIONS_TYPE.valueOf(data.getStringExtra("categoryType")));
						adapter.notifyDataSetChanged();

						DBManager.getInstance(this).updateCategory(category);
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
					R.drawable.bill,
					R.drawable.settings
			};
			for (int i = 0; i < 3; i++)
				subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);

			String[] subButtonTexts = new String[]{"Home", "Accounts list", "Settings"};

			int[][] subButtonColors = new int[][] {
					{ ContextCompat.getColor(this,R.color.accent), ContextCompat.getColor(this, R.color.accent) },
					{ ContextCompat.getColor(this,R.color.md_blue_600), ContextCompat.getColor(this, R.color.md_blue_600) },
					{ ContextCompat.getColor(this,R.color.md_red_600), ContextCompat.getColor(this, R.color.md_red_600) }
			};

			// Now with Builder, you can init BMB more convenient
			new BoomMenuButton.Builder()
					.subButtons(subButtonDrawables, subButtonColors, subButtonTexts)
//					.addSubButton(ContextCompat.getDrawable(this, R.drawable.house), subButtonColors[0], "BoomMenuButton")
//					.addSubButton(ContextCompat.getDrawable(this, R.drawable.bill), subButtonColors[0], "View source code")
//					.addSubButton(ContextCompat.getDrawable(this, R.drawable.settings), subButtonColors[0], "Follow me")
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
						Intent intent = new Intent(CategoryListActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
					if (buttonIndex == 1) {
						Intent intent = new Intent(CategoryListActivity.this, AccountsActivity.class);
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