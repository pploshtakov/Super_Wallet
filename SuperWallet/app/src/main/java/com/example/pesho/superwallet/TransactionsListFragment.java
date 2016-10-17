package com.example.pesho.superwallet;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;
import com.example.pesho.superwallet.model.Transfer;
import com.example.pesho.superwallet.model.UsersManager;
import com.example.pesho.superwallet.myViews.CategoryExpandableAdapter;


import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPagerAdapter;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsListFragment extends Fragment implements ViewFactory {

	public static final int TIMEPERIOD_DAY = 0;
	public static final int TIMEPERIOD_WEEK = 1;
	public static final int TIMEPERIOD_MONTH = 2;
	public static final int TIMEPERIOD_YEAR = 3;
	public static final int TIMEPERIOD_CHOOSE_DAY = 4;
	private int CURRENT_TIMEPERIOD = TIMEPERIOD_DAY;

	private int currentPage = 0;

	private static final int REPORT_INDEX_TOTAL = 0;
	private static final int REPORT_INDEX_INCOME = 1;
	private static final int REPORT_INDEX_EXPENSE = 2;

	LocalDateTime startDate;
	LocalDateTime endDate;

	ArrayList<Transaction> transactions;

	TransactionRecyclerAdapter adapter;
	RecyclerView myRecyclerView;
	ArrayList<TransactionsListCategory> categoriesList;
	CategoryExpandableAdapter mCategoryExpandableAdapter;

	ViewPager verticalPager;
	InfiniteHorizontalPagerAdapter iAdapter;

	TextView periodTV;
	TextView incomeTV;
	TextView expenseTV;
	TextView totalTV;


	LocalDateTime currentDayStart;
	LocalDateTime currentDayEnd;
	LocalDateTime startOfWeek;
	LocalDateTime endOfWeek;
	LocalDateTime startOfMonth;
	LocalDateTime endOfMonth;
	LocalDateTime startOfYear;
	LocalDateTime endOfYear;

    public TransactionsListFragment() {
		Bundle args = getArguments();
		if (args != null) {
			CURRENT_TIMEPERIOD = args.getInt("timePeriod", TIMEPERIOD_DAY);
		}

        // Required empty public constructor
		startDate = new LocalDateTime();
		startDate = startDate.minusYears(10);
		endDate = new LocalDateTime();
		endDate = endDate.plusDays(1).withTime(0,0,0,0);
		Log.e("SuperWallet ", "Fragment from " + startDate + " to " + endDate);
		ArrayList<Transaction> transactions = UsersManager.loggedUser.getTransactions(startDate, endDate, null);
		this.transactions = transactions;
		for (Transaction t: transactions) {
			Log.e("SuperWallet ", "Transaction id: " + t.getTransactionId() + " for DateTime: " + t.getDate());
		}

		currentDayStart = new LocalDateTime();
		currentDayStart = currentDayStart.withTime(0, 0, 0, 0);
		currentDayEnd = currentDayStart.withTime(23, 59, 59, 999);

		startOfWeek = currentDayStart.withDayOfWeek(DateTimeConstants.MONDAY);
		endOfWeek = currentDayEnd.withDayOfWeek(DateTimeConstants.SUNDAY);

		startOfMonth = currentDayStart.withDayOfMonth(1);
		endOfMonth = startOfMonth.plusMonths(1).minusDays(1).withTime(23, 59, 59, 999);

		startOfYear = currentDayStart.withDayOfYear(1);
		endOfYear = startOfYear.plusYears(1).minusDays(1).withTime(23, 59, 59, 999);
    }

	LayoutInflater inflater;
	ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

		this.inflater = inflater;
		this.container = container;

		verticalPager = (ViewPager) inflater.inflate(
				R.layout.fragment_infinite_horizontal, container, false);
		iAdapter = new InfiniteHorizontalPagerAdapter(this, 0);
		verticalPager.setAdapter(iAdapter);
		verticalPager.setCurrentItem(Constants.START_INDEX);
		return verticalPager;
	}

	@Override
	public void onResume() {
		super.onResume();

		verticalPager = (ViewPager) inflater.inflate(R.layout.fragment_infinite_horizontal, container, false);
		iAdapter = new InfiniteHorizontalPagerAdapter(this, 0);
		verticalPager.setAdapter(iAdapter);
		verticalPager.setCurrentItem(Constants.START_INDEX);
		container.addView(verticalPager);
	}

	@Override
	public void onPause() {
		super.onPause();

		container.removeAllViews();
	}

	LocalDateTime currentPeriodStart;
	LocalDateTime currentPeriodEnd;
	StringBuilder dateBuilder = new StringBuilder();

	public String buildDateText(LocalDateTime currentPeriodStart, LocalDateTime currentPeriodEnd) {
		dateBuilder.setLength(0);
		switch (CURRENT_TIMEPERIOD) {
			case TIMEPERIOD_WEEK:
				dateBuilder.append(currentPeriodStart.getDayOfMonth());
				dateBuilder.append('-');
				dateBuilder.append(currentPeriodEnd.getDayOfMonth());
				dateBuilder.append(' ');
				dateBuilder.append(currentPeriodStart.monthOfYear().getAsText(new Locale("bg")).toUpperCase());
				dateBuilder.append(' ');
				dateBuilder.append(currentPeriodStart.getYear());
				break;
			case TIMEPERIOD_MONTH:
				dateBuilder.append(currentPeriodStart.monthOfYear().getAsText(new Locale("bg")).toUpperCase());
				dateBuilder.append(' ');
				dateBuilder.append(currentPeriodStart.getYear());
				break;
			case TIMEPERIOD_YEAR:
				dateBuilder.append(currentPeriodStart.getYear());
				break;
			case TIMEPERIOD_DAY:
			default:
				dateBuilder.append(currentPeriodStart.getDayOfMonth());
				dateBuilder.append(' ');
				dateBuilder.append(currentPeriodStart.monthOfYear().getAsText(new Locale("bg")).toUpperCase());
				dateBuilder.append(' ');
				dateBuilder.append(currentPeriodStart.getYear());
				break;
		}
		return dateBuilder.toString();
	}

	@Override
	public View makeView(int vertical, int horizontal) {
		currentPage = horizontal;

		View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_transactions_list, container, false);

		incomeTV = (TextView) root.findViewById(R.id.main_income_tv);
		expenseTV = (TextView) root.findViewById(R.id.main_expence_tv);
		totalTV = (TextView) root.findViewById(R.id.main_total_tv);

		periodTV = (TextView) root.findViewById(R.id.transaction_list_date);

		myRecyclerView = (RecyclerView) root.findViewById(R.id.transactions_recycler_view);
		myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		categoriesList = generateTransactionsList(horizontal);
		mCategoryExpandableAdapter = new CategoryExpandableAdapter(getActivity(), categoriesList);
		myRecyclerView.setAdapter(mCategoryExpandableAdapter);

		periodTV.setText(buildDateText(currentPeriodStart, currentPeriodEnd));

		double[] reportData = calculateReports(transactions);

		incomeTV.setText(String.valueOf(reportData[REPORT_INDEX_INCOME]));
		expenseTV.setText(String.valueOf(reportData[REPORT_INDEX_EXPENSE]));
		totalTV.setText(String.valueOf(reportData[REPORT_INDEX_TOTAL]));

		if (reportData[REPORT_INDEX_TOTAL] < 0) {
			totalTV.setTextColor(Color.RED);
		} else {
			totalTV.setTextColor(Color.BLUE);
		}

		return root;
	}

	public ArrayList<TransactionsListCategory> generateTransactionsList(int page) {
		switch (CURRENT_TIMEPERIOD) {
			case TIMEPERIOD_WEEK:
				currentPeriodStart = startOfWeek.plusWeeks(page);
				currentPeriodEnd = endOfWeek.plusWeeks(page);
				break;
			case TIMEPERIOD_MONTH:
				currentPeriodStart = startOfMonth.plusMonths(page);
				currentPeriodEnd = endOfMonth.plusMonths(page);
				break;
			case TIMEPERIOD_YEAR:
				currentPeriodStart = startOfYear.plusYears(page);
				currentPeriodEnd = endOfYear.plusYears(page);
				break;
			case TIMEPERIOD_CHOOSE_DAY:
				break;
			case TIMEPERIOD_DAY:
			default:
				currentPeriodStart = currentDayStart.plusDays(page);
				currentPeriodEnd = currentDayEnd.plusDays(page);

				break;
		}
		transactions = UsersManager.loggedUser.getTransactions(currentPeriodStart, currentPeriodEnd, null);

		ArrayList<TransactionsListCategory> categoryList = new ArrayList<>();
		HashMap<String, ArrayList<Transaction>> categoryMap = new HashMap<>();
		for (Transaction tr: transactions) {
			if (tr instanceof Transfer) {
				if (!categoryMap.containsKey("Transfer")) {
					categoryMap.put("Transfer", new ArrayList<Transaction>());
				}
				categoryMap.get("Transfer").add(tr);
			}
			else {
				if (categoryMap.get(tr.getCategory().getCategoryName()) == null) {
					categoryMap.put(tr.getCategory().getCategoryName(), new ArrayList<Transaction>());
				}
				categoryMap.get(tr.getCategory().getCategoryName()).add(tr);
			}
		}
		for (Map.Entry e: categoryMap.entrySet()) {
			TransactionsListCategory pObj = new TransactionsListCategory(e.getKey().toString());

			ArrayList<TransactionsListTransaction> childList = new ArrayList<>();

			if (e.getValue() instanceof ArrayList) {
				ArrayList<Transaction> transactions = (ArrayList<Transaction>) e.getValue();
				for (Transaction tr : transactions) {
					childList.add(new TransactionsListTransaction(tr.getDate(), tr));
				}
			}

			pObj.setChildItemList(childList);
			categoryList.add(pObj);
		}
		if (CURRENT_TIMEPERIOD == TIMEPERIOD_CHOOSE_DAY) {
			CURRENT_TIMEPERIOD = TIMEPERIOD_DAY;
		}
		return categoryList;
	}

	private double[] calculateReports(ArrayList<? extends Transaction> transactions) {
		double income = 0;
		double expense = 0;
		double total;
		if (transactions != null) {
			for (Transaction tr : transactions) {
				if (tr.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Income)) {
					income += tr.getAmount();
				} else if (tr.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Expense)) {
					expense += tr.getAmount();
				}
			}
		}
		total = income - expense;
		double[] reportData = new double[3];
		reportData[REPORT_INDEX_TOTAL] = total;
		reportData[REPORT_INDEX_INCOME] = income;
		reportData[REPORT_INDEX_EXPENSE] = expense;
		return reportData;
	}

	public void changePeriod(int timePeriod, LocalDateTime chooseDay) {
		switch (timePeriod) {
			case TIMEPERIOD_WEEK:
				CURRENT_TIMEPERIOD = TIMEPERIOD_WEEK;
				break;
			case TIMEPERIOD_MONTH:
				CURRENT_TIMEPERIOD = TIMEPERIOD_MONTH;
				break;
			case TIMEPERIOD_YEAR:
				CURRENT_TIMEPERIOD = TIMEPERIOD_YEAR;
				break;
			case TIMEPERIOD_CHOOSE_DAY:
				CURRENT_TIMEPERIOD = TIMEPERIOD_CHOOSE_DAY;
				currentPeriodStart = chooseDay.withTime(0,0,0,0);
				currentPeriodEnd = chooseDay.withTime(23,59,59,999);
				currentDayStart = currentPeriodStart;
				currentDayEnd = currentPeriodEnd;
				break;
			case TIMEPERIOD_DAY:
			default:
				CURRENT_TIMEPERIOD = TIMEPERIOD_DAY;
				break;
		}

		// Refreshing the list doesnt work, the freepager keeps a cache...
//		categoriesList = generateTransactionsList(currentPage);
//		mCategoryExpandableAdapter.setParentList(categoriesList, false);
//		mCategoryExpandableAdapter.notifyParentDataSetChanged(false);
//		myRecyclerView.invalidate();

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.detach(this).attach(this).commit();
	}
}
