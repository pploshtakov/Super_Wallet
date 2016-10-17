package com.example.pesho.superwallet.myViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pesho.superwallet.R;
import com.example.pesho.superwallet.StatisticsActivity;
import com.example.pesho.superwallet.model.Account;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends Fragment implements StatisticsActivity.StatisticsInterface {

    private Account account;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        return rootView;
    }

    @Override
    public void notifyPageChanged(int page) {

    }

    @Override
    public void notifyAccountChanged(Account account) {
        this.account = account;
    }
}
