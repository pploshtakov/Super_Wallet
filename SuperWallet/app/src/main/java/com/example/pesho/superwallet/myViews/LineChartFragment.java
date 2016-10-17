package com.example.pesho.superwallet.myViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.StatisticsActivity;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class LineChartFragment extends Fragment implements StatisticsActivity.StatisticsInterface {

    private LineChartView chart;
    private LineChartData data;

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

    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 24;

    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;

    public LineChartFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

        chart = (LineChartView) rootView.findViewById(R.id.line_chart);

        currentPage = 0;
        currentPeriod = 0;
        currentPeriodStart = currentDayStart;
        currentPeriodEnd = currentDayEnd;

        updateChart(currentPage, account);

        return rootView;
    }

    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void resetViewport(int highestAmount) {
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = -highestAmount;
        v.top = highestAmount;
        v.left = 0;
        switch (currentPeriod) {
            case TIMEPERIOD_WEEK:
                v.right = 7;
                break;
            case TIMEPERIOD_MONTH:
                v.right = 30;
                break;
            case TIMEPERIOD_YEAR:
                v.right = 12;
                break;
            case TIMEPERIOD_DAY:
            default:
                v.right = 24;
                break;
        }
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private int generateData(int page, Account account) {

		int maxPointValueX;

		switch (currentPeriod) {
			case TIMEPERIOD_WEEK:
				currentPeriodStart = startOfWeek.plusWeeks(page);
				currentPeriodEnd = endOfWeek.plusWeeks(page);
				maxPointValueX = currentPeriodStart.dayOfWeek().getMaximumValue();
				break;
			case TIMEPERIOD_MONTH:
				currentPeriodStart = startOfMonth.plusMonths(page);
				currentPeriodEnd = endOfMonth.plusMonths(page);
				maxPointValueX = currentPeriodStart.dayOfMonth().getMaximumValue();
				break;
			case TIMEPERIOD_YEAR:
				currentPeriodStart = startOfYear.plusYears(page);
				currentPeriodEnd = endOfYear.plusYears(page);
				maxPointValueX = currentPeriodStart.monthOfYear().getMaximumValue();
				break;
			case TIMEPERIOD_DAY:
			default:
				currentPeriodStart = currentDayStart.plusDays(page);
				currentPeriodEnd = currentDayEnd.plusDays(page);
				maxPointValueX = currentPeriodStart.hourOfDay().getMaximumValue() + 1;
				break;
		}

        List<Line> lines = new ArrayList<>();

		float accountBallance = 0.0f;
		List<PointValue> values = new ArrayList<>();
		List<Transaction> transactions = UsersManager.loggedUser.getTransactions(currentPeriodStart.minusYears(100), currentPeriodEnd, account);

		Collections.sort(transactions, new Comparator<Transaction>() {
			public int compare(Transaction t1, Transaction t2) {
				return t1.getDate().compareTo(t2.getDate());
			}
		});

		LocalDateTime tDate;

		Transaction closestPastTransaction = null;
		Transaction closestFutureTransaction = null;
		for (Transaction t: transactions) {
			if (t.getDate().isBefore(currentPeriodStart)) {
				if (closestPastTransaction == null) {
					closestPastTransaction = t;
				}
				if (t.getDate().isAfter(closestPastTransaction.getDate())) {
					closestPastTransaction = t;
				}
			}
			else {
				if (t.getDate().isAfter(currentPeriodEnd)) {
					if (closestFutureTransaction == null) {
						closestFutureTransaction = t;
					}
					if (t.getDate().isBefore(closestFutureTransaction.getDate())) {
						closestFutureTransaction = t;
					}
				}
			}
		}

		if (closestPastTransaction == null) { values.add(new PointValue(0,0)); }

		float highestBallance = 0.0f;

		float pointValue;
		for (Transaction t: transactions) {
			// If this transaction is after our end period, skip it.
			if (t.getDate().isAfter(currentPeriodEnd)) { continue; }

			// Calculate amount
			if (t.getTransactionType() == Transaction.TRANSACTIONS_TYPE.Expense) {
				accountBallance -= t.getAmount();
			}
			else if(t.getTransactionType() == Transaction.TRANSACTIONS_TYPE.Income) {
				accountBallance += t.getAmount();
			}
			else {
				if (t.getAccountFrom() == account) {
					accountBallance -= t.getAmount();
				}
				else {
					accountBallance += t.getAmount();
				}
			}

			tDate = t.getDate();
			if (tDate.isAfter(currentPeriodStart) && tDate.isBefore(currentPeriodEnd)) {
				switch (currentPeriod) {
					case TIMEPERIOD_WEEK:
						pointValue = Days.daysBetween(currentPeriodStart, tDate).getDays() + (tDate.getHourOfDay() / 24.0f);
						break;
					case TIMEPERIOD_MONTH:
						pointValue = Days.daysBetween(currentPeriodStart, tDate).getDays() + (tDate.getHourOfDay() / 24.0f);
						break;
					case TIMEPERIOD_YEAR:
						pointValue = Months.monthsBetween(currentPeriodStart, tDate).getMonths() + (tDate.getDayOfMonth() / (float)(tDate.dayOfMonth().getMaximumValue()));
						break;
					case TIMEPERIOD_DAY:
					default:
						pointValue = Hours.hoursBetween(currentPeriodStart, tDate).getHours() + (tDate.getMinuteOfHour() / (float)(tDate.minuteOfHour().getMaximumValue()));
						break;
				}
				values.add(new PointValue(pointValue, accountBallance));
				if (Math.abs(accountBallance) > highestBallance) {
					highestBallance = Math.abs(accountBallance);
				}
			}
			if (t == closestPastTransaction) {
				values.add(new PointValue(0, accountBallance));
			}
		}

		if (currentPeriodEnd.isBefore(new LocalDateTime())) {
			values.add(new PointValue(maxPointValueX, accountBallance));
		}

		if (values.size() == 0) {
			chart.setVisibility(View.GONE);

			if (chart.getRootView() instanceof ViewGroup) {
				if (tv == null) {
					tv = new TextView(getContext());
					tv.setText("No data to display\nfor this period.");
					tv.setGravity(Gravity.CENTER);
					tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
					tv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

					ViewGroup vg = (ViewGroup) chart.getRootView();
					vg.addView(tv);
				}
				else {
					tv.setVisibility(View.VISIBLE);
				}
			}
			return 0;
		}
		else {
			chart.setVisibility(View.VISIBLE);

			if (tv != null) {
				tv.setVisibility(View.GONE);
			}
		}

		Line line = new Line(values);
		line.setColor(ChartUtils.COLORS[0]);
		line.setHasLines(hasLines);
		line.setHasPoints(hasPoints);
		lines.add(line);

        data = new LineChartData(lines);

        List<AxisValue> axisValues = generateAxisValues(currentPeriodStart);
        Axis axisX;
        if (axisValues != null && axisValues.size() > 0) {
            axisX = new Axis(axisValues);
        }
        else {
            axisX = new Axis();
        }
        axisX.setName("Time");

        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("Amount");

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        //data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

		return (int)(highestBallance * 1.2);
    }

    private List<AxisValue> generateAxisValues(LocalDateTime currentPeriodStart) {
        List<AxisValue> axisValues = new ArrayList<>();
        String[] labels;
        switch (currentPeriod) {
            case TIMEPERIOD_WEEK:
                labels = new String[] { String.valueOf(currentPeriodStart.getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(1).getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(2).getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(3).getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(4).getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(5).getDayOfMonth()) , String.valueOf(currentPeriodStart.plusDays(6).getDayOfMonth()) };
                break;
            case TIMEPERIOD_MONTH:
                List<String> daysInMonthLabels = new ArrayList<>();
                LocalDate firstDay = new LocalDate(currentPeriodStart);
                LocalDate nextMonthFirstDay = firstDay.plusMonths(1);
                while (firstDay.isBefore(nextMonthFirstDay)) {
                    daysInMonthLabels.add(String.valueOf(firstDay.getDayOfMonth()));
                    firstDay = firstDay.plusDays(1);
                }
                labels = new String[daysInMonthLabels.size()];
                labels = daysInMonthLabels.toArray(labels);
                break;
            case TIMEPERIOD_YEAR:
                labels = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
				break;
            case TIMEPERIOD_DAY:
            default:
                labels = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
                break;
        }

		for (int i = 0; i < labels.length; ++i) {
			axisValues.add(new AxisValue(i).setLabel(labels[i]));
		}
		if (axisValues.size() > 0) {
			return axisValues;
		}
		else {
			return null;
		}

//        float step = 95.0f/(labels.length - 1);
//        float axisValuePos = 0;
//            axisValues.add(new AxisValue(axisValuePos).setLabel(labels[i]));
//            axisValuePos += step;
//            axisValuePos = Math.min(24, axisValuePos);//don't allow axis value be bigger than 95
    }

    public void updateChart(int currentPage, Account account) {
        generateValues();
        int valuesBorder = generateData(currentPage, account);
        resetViewport(valuesBorder);
    }

    @Override
    public void notifyPageChanged(int page) {
        this.currentPage = page;
		updateChart(currentPage, account);
    }

    @Override
    public void notifyAccountChanged(Account account) {
        this.account = account;
		updateChart(currentPage, account);
    }

	@Override
	public void notifyPeriodTypeChanged(int periodType) {
		this.currentPeriod = periodType;
		updateChart(0, null);
	}

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
