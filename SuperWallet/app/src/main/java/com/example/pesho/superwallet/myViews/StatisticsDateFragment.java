package com.example.pesho.superwallet.myViews;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.StatisticsActivity;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;

import java.util.Locale;

import pro.alexzaitsev.freepager.library.view.infinite.Constants;
import pro.alexzaitsev.freepager.library.view.infinite.InfiniteHorizontalPagerAdapter;
import pro.alexzaitsev.freepager.library.view.infinite.ViewFactory;

import static com.example.pesho.superwallet.StatisticsActivity.StatisticsInterface.TIMEPERIOD_DAY;
import static com.example.pesho.superwallet.StatisticsActivity.StatisticsInterface.TIMEPERIOD_MONTH;
import static com.example.pesho.superwallet.StatisticsActivity.StatisticsInterface.TIMEPERIOD_WEEK;
import static com.example.pesho.superwallet.StatisticsActivity.StatisticsInterface.TIMEPERIOD_YEAR;

public class StatisticsDateFragment  extends Fragment implements ViewFactory {

    private int currentTimePeriod = 0;
    StatisticsNotifier activity;

    LocalDateTime startOfWeek;
    LocalDateTime endOfWeek;
    LocalDateTime startOfMonth;
    LocalDateTime endOfMonth;
    LocalDateTime startOfYear;
    LocalDateTime endOfYear;
    LocalDateTime currentDayStart;
    LocalDateTime currentDayEnd;

    LocalDateTime currentPeriodStart;
    LocalDateTime currentPeriodEnd;

    public StatisticsDateFragment() {
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

    public void setActivity(StatisticsNotifier activity) {
        this.activity = activity;
    }

    InfiniteHorizontalPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewPager verticalPager = (ViewPager) inflater.inflate(
                R.layout.fragment_statistics_date, container, false);
        adapter = new InfiniteHorizontalPagerAdapter(this, 0);
        verticalPager.setAdapter(adapter);
        verticalPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                activity.notifyPageChanged(position - Constants.START_INDEX);
            }
        });
        verticalPager.setCurrentItem(Constants.START_INDEX);

        return verticalPager;
    }

    @Override
    public View makeView(int vertical, int horizontal) {

        View root = getActivity().getLayoutInflater().inflate(R.layout.fragment_date_item, null);
//
//        RelativeLayout rl = new RelativeLayout(getContext());
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rl.setLayoutParams(params);
//
//        TextView tv = new TextView(getActivity());
//        tv.setText("<");
//        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
////        tv.setLayoutParams(params); //causes layout update
//
//        rl.addView(tv, params);
//
//        tv = new TextView(getActivity());
//        tv.setText(">");
////        params = (RelativeLayout.LayoutParams)tv.getLayoutParams();
////        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////        tv.setLayoutParams(params); //causes layout update
//        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//        rl.addView(tv, params);
//
//        TextView textView = new TextView(getActivity());
        TextView textView = (TextView) root.findViewById(R.id.date_field);

        switch (currentTimePeriod) {
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
        textView.setText(buildDateText(currentPeriodStart, currentPeriodEnd));
//        textView.setGravity(Gravity.CENTER_HORIZONTAL);
//        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

//        rl.addView(textView, params);

        return root;
    }

    StringBuilder dateBuilder = new StringBuilder();

    public String buildDateText(LocalDateTime currentPeriodStart, LocalDateTime currentPeriodEnd) {
        dateBuilder.setLength(0);
        switch (currentTimePeriod) {
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

    public void notifyTimePeriodChanged(int periodType) {
        currentTimePeriod = periodType;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public interface StatisticsNotifier {
        void notifyPageChanged(int page);
    }
}
