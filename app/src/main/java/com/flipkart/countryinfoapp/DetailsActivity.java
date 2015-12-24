package com.flipkart.countryinfoapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<String> countries;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//      get access to the launching intent
        Intent launchingIntent = getIntent();
        countries = launchingIntent.getStringArrayListExtra("COUNTRIES");
        selectedPosition = launchingIntent.getIntExtra("SELECTED_POSITION", 0);

//        get access to the view pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentManager manager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                String countryName = countries.get(position);

                WikiFragment wiki = new WikiFragment();
                Bundle bundle = new Bundle();
                bundle.putString("COUNTRY_NAME", countryName);
                wiki.setArguments(bundle);

                return wiki;
            }

            @Override
            public int getCount() {
                return countries.size();
            }
        });
        viewPager.setCurrentItem(selectedPosition);
    }
}
