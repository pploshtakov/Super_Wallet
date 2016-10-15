package com.example.pesho.superwallet;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pesho.superwallet.interfaces.AddTransactionsCommunicator;
import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Transaction;

import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondPageAddingFragment extends Fragment {
    TextView titleTV;
    TextView dateTV;
    private AddTransactionsCommunicator myActivity;
    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Spinner accountFromSpinner;
    Spinner accountToSpinner;
    ImageButton backButton;
    ImageButton saveButton;
    ImageButton placeButton;
    ImageView mapImage;
    EditText descriptionET;

    //location
    private static final int REFRESH_LOCATION = (1000 * 60) * 10; //refresh on 10 minutes
    private static final int MIN_DISTANCE = 100; //min distance for refreshing location in meters
    private static final int REQUEST_PERMISSIONS = 10;
    private ImageButton locationIB;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public SecondPageAddingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_second_page_adding, container, false);
        descriptionET = (EditText) root.findViewById(R.id.addt_description_et);
        //save transaction
        saveButton = (ImageButton) root.findViewById(R.id.second_page_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivity.saveTransaction();
            }
        });
        //back button click listener
        backButton = (ImageButton) root.findViewById(R.id.second_page_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO do not working!!!
                ((Activity) myActivity).getFragmentManager().popBackStack();

            }
        });

        //location
        placeButton = (ImageButton) root.findViewById(R.id.addt_place_button);
        mapImage = (ImageView) root.findViewById(R.id.second_page_map_image);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myActivity.setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, REQUEST_PERMISSIONS);

        } else {
            configurePlaceButton();
        }


        myActivity.registerFragment(this);
        titleTV = (TextView) root.findViewById(R.id.second_page_title);
        //get spinners and set adapter
        ArrayAdapter adapter = new ArrayAdapter((Context) myActivity, android.R.layout.simple_spinner_dropdown_item, myActivity.getAccounts());
        accountFromSpinner = (Spinner) root.findViewById(R.id.addt_account_spinner);
        accountToSpinner = (Spinner) root.findViewById(R.id.addt_account1_spinner);
        accountFromSpinner.setAdapter(adapter);
        accountToSpinner.setAdapter(adapter);


        //set spinners listener
        accountFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = myActivity.getAccounts().get(position);
                myActivity.setAccountFrom(selectedAccount);
                myActivity.setSelectedAccountFrom(accountFromSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account selectedAccount = myActivity.getAccounts().get(position);
                myActivity.setAccountTo(selectedAccount);
                myActivity.setSelectedAccountTo(accountToSpinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleTV.setText(String.valueOf(myActivity.getAmount()));
        //if transactions is transfer
        Intent intent = ((Activity)myActivity).getIntent();
        if (myActivity.getTransactionType().equals(Transaction.TRANSACTIONS_TYPE.Transfer.toString())) {
            accountToSpinner.setVisibility(View.VISIBLE);
        }
        //set current date on dateTV
        dateTV = (TextView) root.findViewById(R.id.addt_date_tv);
        myCalendar = Calendar.getInstance();
        String ct = DateFormat.getDateInstance().format(new Date());
        dateTV.setText(ct);
        myActivity.setTransactionDate(new LocalDateTime());
        //date picker dialog
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myActivity.setTransactionDate(new LocalDateTime(myCalendar));
                updateLabel();
                //dateTV.setText(DateFormat.getInstance().format(myCalendar.getTime()).substring(0, 8));
            }
        };
        //set click listener on dateTV
        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog((Context) myActivity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //edittext get text immediately
        descriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                myActivity.setDescription("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myActivity.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                myActivity.setDescription(s.toString());
            }
        });

        return root;

    }


    private void configurePlaceButton() {
        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", REFRESH_LOCATION, MIN_DISTANCE, locationListener);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurePlaceButton();
                }
        }
    }

    void notifyAmountChanged(double amount) {
        titleTV.setText(String.valueOf(amount));
    }

    void notifyAccountFromChanged(double amount) {
        //TODO set selected account
    }

    void notifyAccountToChanged(double amount) {
        //TODO set selected account
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

    //set format on date text view
    private void updateLabel() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTV.setText(sdf.format(myCalendar.getTime()));
    }

    public void refreshAccountFrom(int selectedAccountFrom) {
        if(selectedAccountFrom != -1)
        accountFromSpinner.setSelection(selectedAccountFrom);
    }

    public void refreshAccountTo(int selectedAccountTo) {
        if (selectedAccountTo != -1)
        accountToSpinner.setSelection(selectedAccountTo);
    }
}
