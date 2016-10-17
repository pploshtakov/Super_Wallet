package com.example.pesho.superwallet;

import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.UsersManager;
import com.example.pesho.superwallet.myViews.LineChartFragment;
import com.example.pesho.superwallet.myViews.PieChartFragment;
import com.example.pesho.superwallet.myViews.StatisticsDateFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity implements StatisticsDateFragment.StatisticsNotifier {

	private static final String TAG_LINE = "LineFragment";
	private static final String TAG_PIE = "PieFragment";

	private boolean shouldShowPie = true;

	StatisticsInterface currentFragment;
	StatisticsDateFragment statisticsDateFragment;

	AccountHeader header;
	Bitmap profileImage;
	private Spinner accountSpinner;
	private ArrayList<String> accountNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.statistics_tab_layout);

		TabLayout.Tab pieTab = tabLayout.newTab();
		pieTab.setText("Pie Chart");
		pieTab.setTag("Pie");
		TabLayout.Tab lineTab = tabLayout.newTab();
		lineTab.setText("Line Chart");
		lineTab.setTag("Line");

		tabLayout.addTab(pieTab);
		tabLayout.addTab(lineTab);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab.getTag().equals("Pie")) {
					switchToPie();
				}
				if (tab.getTag().equals("Line")) {
					switchToLine();
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		//create nav drawer

		profileImage = UsersManager.loggedUser.getProfileImage();
		if (profileImage == null) {
			Log.e("Profile", "//////");
		}
		if (profileImage != null) {
			header = new AccountHeaderBuilder()
					.withActivity(this)
					.withHeaderBackground(R.drawable.header_background)
					.addProfiles(
							new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
									.withEmail(UsersManager.loggedUser.getEmail())
									.withIcon(profileImage)
					)
					.withSelectionListEnabledForSingleProfile(false)
					.build();
		} else {
			header = new AccountHeaderBuilder()
					.withActivity(this)
					.withHeaderBackground(R.drawable.header_background)
					.addProfiles(
							new ProfileDrawerItem().withName(UsersManager.loggedUser.getName())
									.withEmail(UsersManager.loggedUser.getEmail())
					)
					.withSelectionListEnabledForSingleProfile(false)
					.build();
		}

		Drawer drawer = new DrawerBuilder().withActivity(this).withCloseOnClick(true).withSliderBackgroundColorRes(R.color.secondPrimary).withAccountHeader(header).build();
		//create drawer's items
		PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withTextColorRes(R.color.white).withName(R.string.drawer_item_day);
		PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withTextColorRes(R.color.white).withName(R.string.drawer_item_week);
		PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withTextColorRes(R.color.white).withName(R.string.drawer_item_month);
		PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withTextColorRes(R.color.white).withName(R.string.drawer_item_year);
		PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withTextColorRes(R.color.white).withName(R.string.drawer_item_choose_date);
		//add drawer items
		drawer.addItem(item1);
		drawer.addItem(item2);
		drawer.addItem(item3);
		drawer.addItem(item4);
		drawer.addItem(item5);

		//drawer item selected listener
		drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
			@Override
			public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

				switch (position) {
					case 1:
						statisticsDateFragment.notifyTimePeriodChanged(StatisticsInterface.TIMEPERIOD_DAY);
						currentFragment.notifyPeriodTypeChanged(StatisticsInterface.TIMEPERIOD_DAY);
						break;
					case 2:
						statisticsDateFragment.notifyTimePeriodChanged(StatisticsInterface.TIMEPERIOD_WEEK);
						currentFragment.notifyPeriodTypeChanged(StatisticsInterface.TIMEPERIOD_WEEK);
						break;
					case 3:
						statisticsDateFragment.notifyTimePeriodChanged(StatisticsInterface.TIMEPERIOD_MONTH);
						currentFragment.notifyPeriodTypeChanged(StatisticsInterface.TIMEPERIOD_MONTH);
						break;
					case 4:
						statisticsDateFragment.notifyTimePeriodChanged(StatisticsInterface.TIMEPERIOD_YEAR);
						currentFragment.notifyPeriodTypeChanged(StatisticsInterface.TIMEPERIOD_YEAR);
						break;
					case 5:
						Toast.makeText(StatisticsActivity.this, "Date", Toast.LENGTH_SHORT).show();
						return true;
				}

				return true;
			}
		});



		StatisticsDateFragment mCurrentFragment = new StatisticsDateFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.statistics_date_container, mCurrentFragment).commitNow();
		getSupportFragmentManager().executePendingTransactions();
		statisticsDateFragment = mCurrentFragment;
		mCurrentFragment.setActivity(this);

		accountNames = new ArrayList<>();
		accountNames.add("All accounts");
		accountNames.add(UsersManager.loggedUser.getDefaultAccount().getAccountName());
		ArrayList<Account> accounts = UsersManager.loggedUser.getAccounts();
		for (Account acc: accounts) {
			accountNames.add(acc.getAccountName());
		}
		accountSpinner = (Spinner) findViewById(R.id.statistics_accounts_spinner);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accountNames);
		accountSpinner.setAdapter(adapter);

		//spinners listener
		accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				Account selectedAccount = myActivity.getAccounts().get(position);
//				myActivity.setAccountFrom(selectedAccount);
//				myActivity.setSelectedAccountFrom(accountsSpinner.getSelectedItemPosition());
				currentFragment.notifyAccountChanged(UsersManager.loggedUser.getAccount(accountNames.get(position)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});


		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.fragment_place, new LineChartFragment(), TAG_LINE);
		fragmentTransaction.add(R.id.fragment_place, new PieChartFragment(), TAG_PIE);
		fragmentTransaction.commitNow();
		getFragmentManager().executePendingTransactions();

		switchToPie();
	}

	public void onResume() {
		super.onResume();
		if (this.shouldShowPie) {
			switchToPie();
		} else {
			switchToLine();
		}
	}

	private void switchToPie() {
		PieChartFragment fragPie = (PieChartFragment) getSupportFragmentManager().findFragmentByTag(TAG_PIE);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.detach(getSupportFragmentManager().findFragmentByTag(TAG_LINE));
		fragmentTransaction.attach(fragPie);
		fragmentTransaction.commitAllowingStateLoss();
		getSupportFragmentManager().executePendingTransactions();
		currentFragment = fragPie;
	}

	private void switchToLine() {
		LineChartFragment fragLine = (LineChartFragment) getSupportFragmentManager().findFragmentByTag(TAG_LINE);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.detach(getSupportFragmentManager().findFragmentByTag(TAG_PIE));
		fragmentTransaction.attach(fragLine);
		fragmentTransaction.commitAllowingStateLoss();
		getSupportFragmentManager().executePendingTransactions();
		currentFragment = fragLine;
	}

	@Override
	public void notifyPageChanged(int page) {
		currentFragment.notifyPageChanged(page);
	}

	public interface StatisticsInterface {
		int TIMEPERIOD_DAY = 0;
		int TIMEPERIOD_WEEK = 1;
		int TIMEPERIOD_MONTH = 2;
		int TIMEPERIOD_YEAR = 3;

		void notifyPageChanged(int page);
		void notifyAccountChanged(Account account);
		void notifyPeriodTypeChanged(int periodType);
	}
}
