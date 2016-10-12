package com.example.pesho.superwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.pesho.superwallet.model.Transaction;
import com.example.pesho.superwallet.model.UsersManager;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class PieStatisticsActivity extends AppCompatActivity {

	private PieChartView pie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pie_statistics);

		pie = (PieChartView) findViewById(R.id.pie_statistics_view);

		PieChartData data = new PieChartData();

		data.setHasLabels(true);
		data.setHasLabelsOutside(true);

		LocalDateTime start = LocalDateTime.now().withTime(0,0,0,0);
		LocalDateTime end = LocalDateTime.now().withTime(23,59,59,999);


		List<SliceValue> list = new ArrayList<SliceValue>();
		ArrayList<Transaction> transactions = UsersManager.getInstance(this).loggedUser.getTransactions(start, end);
		for (Transaction t: transactions) {
			SliceValue val = new SliceValue((float)t.getAmount() , ChartUtils.pickColor());

			Log.e("SuperWallet ", "Amount: " + t.getAmount());
			if (t.getCategory() != null) {
				val.setLabel(t.getCategory().getCategoryName() + " " + val.getValue());
			}
			else {
				val.setLabel(t.getTransactionType().toString() + " " + val.getValue());
			}
			list.add(val);
		}
		data.setValues(list);
		pie.setPieChartData(data);

	}
}
