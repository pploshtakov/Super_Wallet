package com.example.pesho.superwallet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
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
import android.widget.Toast;

import com.example.pesho.superwallet.model.Category;
import com.example.pesho.superwallet.model.DBManager;

public class CategoryListActivity
	extends AppCompatActivity
	implements OnDragListener, OnItemLongClickListener, OnItemClickListener {

	private static final int MODIFY_CATEGORY_REQUEST = 1;

	ArrayList<Category> categories;

	boolean pickingCategory = false;

	private BaseAdapter adapter;
	private int draggedIndex = -1;
	private GridView gridView;

	private Button addCategoryButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);

		// Create the categories array and get the gridView
		categories = new ArrayList<>();
		gridView = (GridView) findViewById(R.id.category_grid_view);
		addCategoryButton = (Button) findViewById(R.id.add_category_button);

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

		// Check the intent if we're just picking a category
		Intent intent = getIntent();
		if (intent != null) {
			pickingCategory = intent.getBooleanExtra("pickingCategory", false);
		}

		// If we are, add the default categories to the list
		// TODO hide other buttons and trash can, disable dragging
		if (pickingCategory) {
			ArrayList<Category> defaultCategories = DBManager.getInstance(this).loadDefaultCategories();
			categories.addAll(defaultCategories);

			addCategoryButton.setVisibility(View.GONE);
		}
		// If we're not picking a category, add dragging and trash can and dont add the default categories
		// to the list.
		else {
			addCategoryButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CategoryListActivity.this, CategoryModifierActivity.class);
					startActivity(intent);
				}
			});

			gridView.setOnItemLongClickListener(CategoryListActivity.this);

			addCategoryButton.setVisibility(View.VISIBLE);
		}

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

		if (!pickingCategory) {
			intent = new Intent(CategoryListActivity.this, CategoryModifierActivity.class);
			intent.putExtra("categoryId", categories.get(position).getCategoryId());
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
				Toast.makeText(this, "stats", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_home:
				Intent home = new Intent(this, MainActivity.class);
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
				Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_actions, menu);
		MenuItem itemView = menu.findItem(R.id.action_category);
		itemView.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}
}