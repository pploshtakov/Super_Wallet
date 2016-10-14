package com.example.pesho.superwallet;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.TransactionsListCategory;
import com.example.pesho.superwallet.model.TransactionsListTransaction;
import com.example.pesho.superwallet.model.Transfer;
import com.example.pesho.superwallet.model.UsersManager;
import com.example.pesho.superwallet.myViews.CategoryExpandableAdapter;


import org.joda.time.DateTimeConstants;
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

	private static final int TIMEPERIOD_DAY = 0;
	private static final int TIMEPERIOD_WEEK = 1;
	private static final int TIMEPERIOD_MONTH = 2;
	private static final int TIMEPERIOD_YEAR = 3;
	private int CURRENT_TIMEPERIOD = TIMEPERIOD_DAY;

	private static final int REPORT_INDEX_TOTAL = 0;
	private static final int REPORT_INDEX_INCOME = 1;
	private static final int REPORT_INDEX_EXPENSE = 2;

	LocalDateTime startDate;
	LocalDateTime endDate;

	ArrayList<Transaction> transactions;

	TransactionRecyclerAdapter adapter;
	RecyclerView myRecyclerView;

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
        // Required empty public constructor
		startDate = new LocalDateTime();
		startDate = startDate.minusYears(10);
		endDate = new LocalDateTime();
		endDate = endDate.plusDays(1).withTime(0,0,0,0);
		Log.e("SuperWallet ", "Fragment from " + startDate + " to " + endDate);
		ArrayList<Transaction> transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
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

		ViewPager verticalPager = (ViewPager) inflater.inflate(
				R.layout.fragment_infinite_horizontal, container, false);
		InfiniteHorizontalPagerAdapter iAdapter = new InfiniteHorizontalPagerAdapter(this, 0);
		verticalPager.setAdapter(iAdapter);
		verticalPager.setCurrentItem(Constants.START_INDEX);
		return verticalPager;
	}

	LocalDateTime currentPeriodStart;
	LocalDateTime currentPeriodEnd;
	@Override
	public View makeView(int vertical, int horizontal) {
//		Button btn = new Button(getActivity());
//		btn.setText("Horizontal " + horizontal);
//		return btn;

		View root = LayoutInflater.from(getContext()).inflate(R.layout.fragment_transactions_list, container, false);

		incomeTV = (TextView) root.findViewById(R.id.main_income_tv);
		expenseTV = (TextView) root.findViewById(R.id.main_expence_tv);
		totalTV = (TextView) root.findViewById(R.id.main_total_tv);

		periodTV = (TextView) root.findViewById(R.id.transaction_list_date);

		myRecyclerView = (RecyclerView) root.findViewById(R.id.transactions_recycler_view);
		myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		switch (CURRENT_TIMEPERIOD) {
			case TIMEPERIOD_WEEK:
				currentPeriodStart = startOfWeek.plusWeeks(horizontal);
				currentPeriodEnd = endOfWeek.plusWeeks(horizontal);
				break;
			case TIMEPERIOD_MONTH:
				currentPeriodStart = startOfMonth.plusMonths(horizontal);
				currentPeriodEnd = endOfMonth.plusMonths(horizontal);
				break;
			case TIMEPERIOD_YEAR:
				currentPeriodStart = startOfYear.plusYears(horizontal);
				currentPeriodEnd = endOfYear.plusYears(horizontal);
				break;
			case TIMEPERIOD_DAY:
			default:
				currentPeriodStart = currentDayStart.plusDays(horizontal);
				currentPeriodEnd = currentDayEnd.plusDays(horizontal);

				break;
		}
		transactions = UsersManager.loggedUser.getTransactions(currentPeriodStart, currentPeriodEnd);
		periodTV.setText((new LocalDate(currentPeriodStart)).toString() + " - " + (new LocalDate(currentPeriodEnd)).toString());

		ArrayList<ParentObject> categoryList = new ArrayList<>();
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

			ArrayList<Object> childList = new ArrayList<>();

			if (e.getValue() instanceof ArrayList) {
				ArrayList<Transaction> transactions = (ArrayList) e.getValue();
				for (Transaction tr : transactions) {
					childList.add(new TransactionsListTransaction(tr.getDate()));
					Log.e("SuperWallet ", "Transaction id: " + tr.getTransactionId());
				}
			}

			pObj.setChildObjectList(childList);
			categoryList.add(pObj);
		}
//		for (int i = 0; i < 5; i++) {
//			TransactionsListCategory pObj = new TransactionsListCategory("Category " + i);
//			ArrayList<Object> childList = new ArrayList<Object>();
//			childList.add(new TransactionsListTransaction(new LocalDateTime()));
//			pObj.setChildObjectList(childList);
//			categoryList.add(pObj);
//		}
		CategoryExpandableAdapter mCategoryExpandableAdapter = new CategoryExpandableAdapter(getActivity(), categoryList);
		mCategoryExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
		mCategoryExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
		mCategoryExpandableAdapter.setParentAndIconExpandOnClick(true);

		//adapter = new TransactionRecyclerAdapter(transactions, myRecyclerView);
		myRecyclerView.setAdapter(mCategoryExpandableAdapter);

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

	private void setPeriod(LocalDateTime startDate, LocalDateTime endDate) {
		String myFormat = "dd.MM.yyyy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		String start = sdf.format(startDate.toDate());
		String end = sdf.format(endDate.toDate());
		periodTV.setText(start + " - " + end);
	}

	public void refreshList(LocalDateTime startDate, LocalDateTime endDate) {
		transactions = UsersManager.loggedUser.getTransactions(startDate, endDate);
		adapter = (TransactionRecyclerAdapter) myRecyclerView.getAdapter();
		adapter.setNewList(transactions);
		adapter.notifyDataSetChanged();
		calculateReports(transactions);
		setPeriod(startDate, endDate);
	}

}
