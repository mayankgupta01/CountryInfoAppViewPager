package com.flipkart.countryinfoapp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
// */
//// One way to add listener is to implement onclicn listener interface.
//    Another way is to use anonymous class, Why? because with implementing interface, onclick method
//        needs to be overridden, if we have more num of buttons then method will have lots of ifs
public class CountryListFragment extends Fragment {

    private ListView countryListTextView;
    private Button addButton;
    AddCountryDelegate delegate;
    private ArrayList<String> countries;

//    Day 3: Pass 1: Use Adapter Object for populating the list view
//    private ArrayAdapter<String> countryAdapter;

//    Day 3: Pass 2: Use Custom Adapter instead of ArrayAdapter
    private CountryAdapter countryAdapter;
    public CountryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View fragView = inflater.inflate(R.layout.fragment_country_list2, container, false);

        // get references to button and editText
        countryListTextView = (ListView)  fragView.findViewById(R.id.listView);
//        Day 3: Pass 5: Adding onclick listener to listView so that it can open the wikipage for the country
        countryListTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Day 4 : Using ViewPager using new DetailsActivity
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putStringArrayListExtra("COUNTRIES", countries);
                intent.putExtra("SELECTED_POSITION", position);
                startActivity(intent);

//                Day 3 Code
//                String countryName = countries.get(position);

//                WikiFragment wiki = new WikiFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("COUNTRY_NAME", countryName);
//                wiki.setArguments(bundle);

//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
////                remove country list fragment
//                transaction.remove(CountryListFragment.this);
////                add wiki fragment
//                transaction.add(R.id.mainLayout, wiki, "WF");
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });

//     Day 4: Removing add button from here as it has been added to options menu
//        addButton = (Button)  fragView.findViewById(R.id.addButton);
//
//        //attach
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Pass 3 Code:
//                if(delegate != null) {
//                    delegate.switchToAddCountry();
//                }
//                Pass 2 Code:
//                MainActivity ma = (MainActivity) getActivity();
//                ma.switchToAddCountry();
//                Pass 1 Code:
//                FragmentManager manager = getFragmentManager();
//                AddCountryFragment ac = new AddCountryFragment();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,R.anim.fade_in, R.anim.fade_out);
//                transaction.remove(CountryListFragment.this);
//                transaction.add(R.id.mainLayout, ac, "AC");
//                // This overrides the back button. Otherwise, the activity gets closed
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        //Day 3: Pass 1:Create Adapter for the LIst view. row.xml for displaying each row has been defined.
//        countryAdapter = new ArrayAdapter<String>(getActivity(),R.layout.row,R.id.countryName,countries);

//        Day 3: Pass 2: Use Custom Adapter. But when we implement caching using LRU, it will become useless.
//        Because every time we add a new country, creatView happens again.So, new adapter is getting created.
        countryAdapter = new CountryAdapter(getActivity(),countries);
        //attach the adapter to the list view
        countryListTextView.setAdapter(countryAdapter);

//        To enable options menu for fragment, we need to call below. For Activity, no need to call.
        setHasOptionsMenu(true);

        return fragView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      Day 3 : Pass 4: Trying to fix the cache misses. OnCreate gets called only once
//        countryAdapter = new CountryAdapter(getActivity(),countries);
//        //attach the adapter to the list view
//        countryListTextView.setAdapter(countryAdapter);


        if( savedInstanceState != null) {
            countries = savedInstanceState.getStringArrayList("COUNTRIES");
        }else {
            countries = new ArrayList<String>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == Activity.RESULT_OK) {
            String countryName = data.getStringExtra("COUNTRY_NAME");
            countries.add(countryName);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("COUNTRIES", countries);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.country_list_menu, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addAction) {
            if( delegate != null) {
                delegate.switchToAddCountry();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        //        Day 3: Updating the UI using adapter
        countryAdapter.notifyDataSetChanged();

//        Code implementation of Day 2.
//        StringBuffer buffer = new StringBuffer();
//        for(String name : countries) {
//            buffer.append(name);
//            buffer.append("\n");
//        }
//        countryListTextView.setText(buffer.toString());
    }
}
