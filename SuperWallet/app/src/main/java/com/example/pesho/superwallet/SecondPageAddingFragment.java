package com.example.pesho.superwallet;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pesho.superwallet.interfaces.AddTransactionsCommunicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPageAddingFragment extends Fragment {
    TextView titleTV;
    private AddTransactionsCommunicator myActivity;

    public SecondPageAddingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_second_page_adding, container, false);
        myActivity.registerFragment(this);
        titleTV = (TextView) root.findViewById(R.id.second_page_title);
        titleTV.setText(String.valueOf(myActivity.getAmount()));
        return root;
    }

    void notifyAmountChanged(double amount) {
        titleTV.setText(String.valueOf(amount));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (AddTransactionsCommunicator) context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
