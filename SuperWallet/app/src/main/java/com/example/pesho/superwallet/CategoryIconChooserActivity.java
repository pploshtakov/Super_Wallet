package com.example.pesho.superwallet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class CategoryIconChooserActivity extends AppCompatActivity
	implements AdapterView.OnItemClickListener {

	private ArrayList<Integer> drawables;

	private GridView gridView;
	private BaseAdapter adapter;

	private Button cancelButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_icon_chooser);

		gridView = (GridView) findViewById(R.id.iconChooserGridView);

		drawables = new ArrayList<Integer>();

		drawables.add(R.drawable.taxi);
		drawables.add(R.drawable.phone);
		drawables.add(R.drawable.house);
		drawables.add(R.drawable.health);
		drawables.add(R.drawable.ball);
		drawables.add(R.drawable.bill);
		drawables.add(R.drawable.bus);
		drawables.add(R.drawable.car);
		drawables.add(R.drawable.coffee);
		drawables.add(R.drawable.fitness);
		drawables.add(R.drawable.fork_spoon);
		drawables.add(R.drawable.fridge);
		drawables.add(R.drawable.taxi);

		drawables.add(R.drawable.taxi);
		drawables.add(R.drawable.phone);
		drawables.add(R.drawable.house);
		drawables.add(R.drawable.health);
		drawables.add(R.drawable.ball);
		drawables.add(R.drawable.bill);
		drawables.add(R.drawable.bus);
		drawables.add(R.drawable.car);
		drawables.add(R.drawable.coffee);
		drawables.add(R.drawable.fitness);
		drawables.add(R.drawable.fork_spoon);
		drawables.add(R.drawable.fridge);
		drawables.add(R.drawable.taxi);
		drawables.add(R.drawable.taxi);
		drawables.add(R.drawable.phone);
		drawables.add(R.drawable.house);
		drawables.add(R.drawable.health);
		drawables.add(R.drawable.ball);
		drawables.add(R.drawable.bill);
		drawables.add(R.drawable.bus);
		drawables.add(R.drawable.car);
		drawables.add(R.drawable.coffee);
		drawables.add(R.drawable.fitness);
		drawables.add(R.drawable.fork_spoon);
		drawables.add(R.drawable.fridge);
		drawables.add(R.drawable.taxi);

		gridView = (GridView) findViewById(R.id.iconChooserGridView);
		cancelButton = (Button) findViewById(R.id.iconChooserCancelButton);

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});

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
					view = new ImageView(CategoryIconChooserActivity.this);
				}
				view.setImageResource(drawables.get(position));
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
				return drawables.get(position);
			}

			@Override
			// How many items are in the data set represented by this Adapter.
			public int getCount() {
				return drawables.size();
			}
		};

		gridView.setAdapter(adapter);
		// Set on Item Click (boolean pickingCategory determines the behavior)
		gridView.setOnItemClickListener(CategoryIconChooserActivity.this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra("iconResource", drawables.get(position));
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
