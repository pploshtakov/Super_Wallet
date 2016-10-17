package com.example.pesho.superwallet.myViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.StatisticsActivity;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieChartFragment extends Fragment implements StatisticsActivity.StatisticsInterface {

    private PieChartView pie;
    private TextView tv;

    private int currentPage;
    private Account account;

    private int currentPeriod = 0;

    private LocalDateTime startOfWeek;
    private LocalDateTime endOfWeek;
    private LocalDateTime startOfMonth;
    private LocalDateTime endOfMonth;
    private LocalDateTime startOfYear;
    private LocalDateTime endOfYear;
    private LocalDateTime currentDayStart;
    private LocalDateTime currentDayEnd;

    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;

    public PieChartFragment() {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        pie = (PieChartView) rootView.findViewById(R.id.chart);
		tv = null;

        currentPage = 0;
        updatePie(currentPage, null);

        return rootView;
    }

    public List<SliceValue> getPieData(int page, Account account) {
        switch (currentPeriod) {
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
            case TIMEPERIOD_DAY:
            default:
                currentPeriodStart = currentDayStart.plusDays(page);
                currentPeriodEnd = currentDayEnd.plusDays(page);

                break;
        }

        List<SliceValue> list = new ArrayList<>();
        ArrayList<Transaction> transactions = UsersManager.loggedUser.getTransactions(currentPeriodStart, currentPeriodEnd, account);
        double totalAmount = 0.0;
        HashMap<String, ArrayList<Transaction>> categories = new HashMap<>();
        HashMap<String, Double> categoryAmount = new HashMap<>();
        for (Transaction t: transactions) {
            if (t.getTransactionType().toString().equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {
                if (categories.get("Transfer") == null) {
                    categories.put("Transfer", new ArrayList<Transaction>());
                    categoryAmount.put("Transfer", 0.0);
                }
                categories.get("Transfer").add(t);
                categoryAmount.put("Transfer", categoryAmount.get("Transfer") + t.getAmount());
            }
            else {
                if (categories.get(t.getCategory().getCategoryName()) == null) {
                    categories.put(t.getCategory().getCategoryName(), new ArrayList<Transaction>());
                    categoryAmount.put(t.getCategory().getCategoryName(), 0.0);
                }
                categories.get(t.getCategory().getCategoryName()).add(t);
                categoryAmount.put(t.getCategory().getCategoryName(), categoryAmount.get(t.getCategory().getCategoryName()) + t.getAmount());
            }
            totalAmount += t.getAmount();
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        for (String s: categories.keySet()) {
            ArrayList<Transaction> categoryTransactions = categories.get(s);
            float amount = 0;
            for (Transaction t: categoryTransactions) {
                amount += (float)t.getAmount();
            }
            SliceValue val = new SliceValue( (float) ((amount / totalAmount) * 100.0) , ChartUtils.pickColor());

//            if (t.getCategory() != null) {
                val.setLabel(s + " " + df.format(val.getValue()) + "%");
//            }
//            else {
//                val.setLabel(s + " " + val.getValue());
//            }
            list.add(val);
        }
        return list;
    }

    public void updatePie(int page, Account account) {
        if (pie == null) { return; }

        List<SliceValue> list = getPieData(page, account);

        if (list.size() > 0) {
            PieChartData data = new PieChartData(list);
            data.setHasLabels(true);
            data.setHasLabelsOutside(false);
            data.setHasCenterCircle(true);

            pie.setPieChartData(data);
            pie.setVisibility(View.VISIBLE);

            if (tv != null) {
                tv.setVisibility(View.GONE);
            }
        }
        else {
            pie.setVisibility(View.GONE);

            if (pie.getRootView() instanceof ViewGroup) {
                if (tv == null) {
                    tv = new TextView(getContext());
                    tv.setText("No data to display\nfor this period.");
                    tv.setGravity(Gravity.CENTER);
                    tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    tv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                    ViewGroup vg = (ViewGroup) pie.getRootView();
                    vg.addView(tv);
                }
                else {
                    tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void notifyPageChanged(int page) {
        currentPage = page;
        updatePie(currentPage, account);
    }

    @Override
    public void notifyAccountChanged(Account account) {
        this.account = account;
        updatePie(currentPage, account);
    }

    @Override
    public void notifyPeriodTypeChanged(int periodType) {
        this.currentPeriod = periodType;
    }

}
