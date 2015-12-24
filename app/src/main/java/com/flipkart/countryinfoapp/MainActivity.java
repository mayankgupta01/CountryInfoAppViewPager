package com.flipkart.countryinfoapp;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements AddCountryDelegate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this is needed so that two copies of fragment is not there. Ex. launch app, then change orientation,
        // so the main activity gets destroyed and created again,
        // Here, it will again call load Fragment even though the fragment is already there.
        if (savedInstanceState == null) {
            loadCountryListFragment();
        }else {
            // Pass 3: Withtout this code , when we rotate device, Add button doesnt work.
            // the delegate becomes null, because 2nd time loadCountryListFragment is not called.
            // To overcome this, we need to set delegate again. This time, Fragment manager already
            // creates the fragment for us, so, no need to make the object again. We can directly get fragment by tag here.
            // get access to the fragment manager, use support manager so that older versions of android can also use these fragments
            FragmentManager manager = getSupportFragmentManager();

            CountryListFragment fragment = (CountryListFragment) manager.findFragmentByTag("CLF");
            fragment.delegate = this;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//     Day 4: Pass 1: Better way to implement is to have a resource file under menu and inflate from xml resource
//        menu.add("Add");
//        menu.add("Settings");
//      Day 4 : Pass 2 : Using resource file, Even better way is to have it in fragment instead of activity
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void loadCountryListFragment() {
        //create fragment object
        CountryListFragment clf = new CountryListFragment();

        //Pass 3 code:
        //set the main activity as the delegate for the country list fragment
        clf.delegate = this;

        // get access to the fragment manager, use support manager so that older versions of android can also use these fragments
        FragmentManager manager = getSupportFragmentManager();

        //begin a fragment transaction
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

        //add fragment to the transaction
        transaction.add(R.id.mainLayout, clf, "CLF");

        transaction.commit();
    }

    public void switchToAddCountry() {
        FragmentManager manager = getSupportFragmentManager();
        AddCountryFragment ac = new AddCountryFragment();

        // find the country list fragment
        Fragment clf = manager.findFragmentByTag("CLF");
        FragmentTransaction transaction = manager.beginTransaction();

        //set the country list fragment as the target for add country fragment
        ac.setTargetFragment(clf,101);

        if(clf !=null) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,R.anim.fade_in, R.anim.fade_out);
            transaction.remove(clf);
            transaction.add(R.id.mainLayout, ac, "AC");
            // This overrides the back button. Otherwise, the activity gets closed
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
