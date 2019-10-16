package com.ArmGuide.tourapplication.ui.home;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ArmGuide.tourapplication.R;
import com.ArmGuide.tourapplication.models.Filter;
import com.ArmGuide.tourapplication.ui.createTour.CreateTourActivity;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    private RadioGroup radioGroupBus, radioGroupFood, radioGroupGuide, radioGroupWine, radioGroupWifi;
    private TextView textViewDateFrom, textViewDateTo, textViewPriceFrom, textViewPriceTo;
    private Button buttonSkip, buttonConfirm;
    private CrystalRangeSeekbar seekbar;

    private String dateStartFrom, dateEndTo;
    private int priceFrom, priceTo;
    private boolean transportMust, foodMust, guideMust, wineMust, wifiMust;

    private String userKey, placeName, placeKey;

    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userKey = getArguments().getString("userKey");
        placeName = getArguments().getString("placeName");
        placeKey = getArguments().getString("placeKey");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //view initialization
        radioGroupBus = view.findViewById(R.id.radioGroup_bus);
        radioGroupFood = view.findViewById(R.id.radioGroup_food);
        radioGroupGuide = view.findViewById(R.id.radioGroup_Guide);
        radioGroupWifi = view.findViewById(R.id.radioGroup_Wifi);
        radioGroupWine = view.findViewById(R.id.radioGroup_Wine);
        textViewDateFrom = view.findViewById(R.id.tv_selectDateFrom);
        textViewDateTo = view.findViewById(R.id.tv_selectDateTo);
        textViewPriceFrom = view.findViewById(R.id.tv_PriceIndicateFrom);
        textViewPriceTo = view.findViewById(R.id.tv_PriceIndicateTo);
        buttonSkip = view.findViewById(R.id.btn_SkipFilter);
        buttonConfirm = view.findViewById(R.id.btn_ConfirmFilter);
        seekbar = view.findViewById(R.id.seekBar);
        //

        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                priceFrom = minValue.intValue() * 1000;
                priceTo = maxValue.intValue() * 1000;
                String maxPrice;
                if (priceTo == 100000) {
                    priceTo = 100000000;
                    maxPrice = "to >100,000";
                } else
                    maxPrice = "to  " + (priceTo);

                String minPrice = "from  " + (priceFrom);
                textViewPriceFrom.setText(minPrice);
                textViewPriceTo.setText(maxPrice);
            }
        });


        textViewDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCalendar(textViewDateFrom);
            }
        });

        textViewDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                callCalendar(textViewDateTo);
            }
        });

        radioGroupBus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_bus_non_mandatory:
                        transportMust = false;
                        break;
                    case R.id.radio_bus_Yes:
                        transportMust = true;
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupFood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_food_Mandatory:
                        foodMust = false;
                        break;
                    case R.id.radio_food_Yes:
                        foodMust = true;
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupGuide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_guide_Mandatory:
                        guideMust = false;
                        break;
                    case R.id.radio_guide_Yes:
                        guideMust = true;
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupWine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_wine_Mandatory:
                        wineMust = false;
                        break;
                    case R.id.radio_wine_Yes:
                        wineMust = true;
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupWifi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_wifi_Mandatory:
                        wifiMust = false;
                        break;
                    case R.id.radio_wifi_Yes:
                        wifiMust = true;
                        break;
                    default:
                        break;
                }
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new Filter();
                filter.setPlaceName(placeName);
                filter.setDateFrom(dateStartFrom);
                filter.setDateTo(dateEndTo);
                filter.setPriceFrom(priceFrom);
                filter.setPriceTo(priceTo);
                filter.setFoodMust(foodMust);
                filter.setGuideMust(guideMust);
                filter.setWifiMust(wifiMust);
                filter.setWineMust(wineMust);
                filter.setTransportMust(transportMust);

                FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                        .child("SubscribedToursCriteria").child(placeKey).setValue(filter);
                Toast.makeText(view.getContext(), "You have successfully subscribed on new tours with specific criteria!", Toast.LENGTH_SHORT).show();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().popBackStack();
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new Filter();
                filter.setPlaceName(placeName);
                filter.setDateFrom(null);
                filter.setDateTo(null);
                filter.setPriceFrom(0);
                filter.setPriceTo(100000000);
                filter.setFoodMust(false);
                filter.setGuideMust(false);
                filter.setWifiMust(false);
                filter.setWineMust(false);
                filter.setTransportMust(false);

                FirebaseDatabase.getInstance().getReference().child("Tourists").child(userKey)
                        .child("SubscribedToursCriteria").child(placeKey).setValue(filter);
                Toast.makeText(view.getContext(), "You have successfully subscribed on new tours!", Toast.LENGTH_SHORT).show();
                ((FragmentActivity) view.getContext()).getSupportFragmentManager().popBackStack();
            }
        });


    }

    private int compareDates(String from, String to) {
        int result = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateFrom = formatter.parse(from);
            Date dateTo = formatter.parse(to);
            result = dateFrom.compareTo(dateTo);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    private void callCalendar(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Dialog dialog = new DatePickerDialog(textView.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (textView == textViewDateTo) {
                    dateEndTo = day + "/" + (month + 1) + "/" + year;
                    int compare;
                    if (dateStartFrom != null) {
                        compare = compareDates(dateStartFrom, dateEndTo);
                        if (compare > 0) {
                            dateEndTo = null;
                            Toast.makeText(textView.getContext(), "Wrong selection, date TO must be later than FROM", Toast.LENGTH_LONG).show();
                            callCalendar(textView);
                        }
                    }
                    textView.setText(dateEndTo);
                } else if (textView == textViewDateFrom) {
                    dateStartFrom = day + "/" + (month + 1) + "/" + year;
                    int compare;
                    if (dateEndTo != null) {
                        compare = compareDates(dateStartFrom, dateEndTo);
                        if (compare > 0) {
                            dateStartFrom = null;
                            Toast.makeText(textView.getContext(), "Wrong selection, date TO must be later than FROM", Toast.LENGTH_LONG).show();
                            callCalendar(textView);
                        }
                    }
                    textView.setText(dateStartFrom);
                }
            }
        }, year, month, day);

        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (textView == textViewDateTo) {
                    textViewDateTo.setText("Date to");
                    dateEndTo = null;
                } else if (textView == textViewDateFrom) {
                    textViewDateFrom.setText("Date from");
                    dateStartFrom = null;
                }
            }
        });
        dialog.show();
    }
}
