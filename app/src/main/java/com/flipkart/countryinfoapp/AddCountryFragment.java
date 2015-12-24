package com.flipkart.countryinfoapp;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCountryFragment extends android.support.v4.app.Fragment {

    private EditText countryNameEditText;
    private Button doneButton;
    private Button cancelButton;

    public AddCountryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView =  inflater.inflate(R.layout.fragment_add_country, container, false);
        countryNameEditText = (EditText) fragView.findViewById(R.id.addCountryText);
        doneButton = (Button) fragView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryName = countryNameEditText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("COUNTRY_NAME", countryName);

                getTargetFragment().onActivityResult(101, Activity.RESULT_OK, intent);

                //reverse the transaction on the backstack
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                manager.popBackStack();
            }
        });
        cancelButton = (Button) fragView.findViewById(R.id.cancelButton);

        return  fragView;
    }


}
