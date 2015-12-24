package com.flipkart.countryinfoapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by mayank.gupta on 16/12/15.
 */
public class CountryAdapter extends BaseAdapter {

//    Adapter needs two things: Context - Needed to access the resources. And an array for data.
    private Context context;
    private ArrayList<String> countries;
    private static String TAG = "CountryAdapter";

//    Day 3 : Pass 4 : Perf improvement, implementing LRU cache for images. so that we dont need to do i/o again.
//    Recommended cache size is - 1/8th of available RAM.
    LruCache<String, Bitmap> cache;

    private static class ViewHolder {
        TextView textview;
        ImageView imageView;
    }

    public CountryAdapter(Context context, ArrayList<String> countries) {
        this.context = context;
        this.countries = countries;

//      Day 3 : Pass 4: implement sizeOf method to let LRU cache know the size of bitmap being added.
//      Before adding the image, it will check the bitmap size and available size and make the decision.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory /8 ;

        Log.i(TAG, "cache size is -- " + cacheSize);
        cache =new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        In Fragment class, we have access to the LayoutInflater in onCreateView method, in adapter we do not.
//        So , we need to define it.
        View mainView;

//        Day 3: Pass 1 code

//        Log.i(TAG, "getView(" + position + ")");
//        get access to the layout inflater
//        LayoutInflater infater = LayoutInflater.from(context);

//        inflate row.xml to create view for the listview row. This is poor implementation,
// because on scroll new view are getting inflated and old views are being gced. scrolling isn't
// smooth anymore. Instead, we can recycle views, so as row(0) goes out of view, listView gives
// the object to Adapter and says that instead of creating new view object just update this one and give me.
//        mainView = infater.inflate(R.layout.row, null);

//        Day 3: Pass 2: Better implementation of Adapter
        if(convertView == null) {
            Log.i(TAG, "getView(" + position + ")");
//        get access to the layout inflater
            LayoutInflater infater = LayoutInflater.from(context);

            mainView = infater.inflate(R.layout.row, null);

//            Day 3 : pass 3: optimization of findViewbyId using ViewHolder
            ViewHolder vh = new ViewHolder();
            vh.textview = (TextView) mainView.findViewById(R.id.countryName);
            vh.imageView = (ImageView) mainView.findViewById(R.id.countryFlagImage);

//            attach view holder to the view
            mainView.setTag(vh);

        }else {
            mainView = convertView;
        }

        String countryName = countries.get(position);

//      Day 3: Pass 2: using findViewById each time getView method is called. This becomes expensive.
//        TextView tv = (TextView) mainView.findViewById(R.id.countryName);
//        ImageView iv = (ImageView) mainView.findViewById(R.id.countryFlagImage);

//        display the country name on the textview
//        tv.setText(countryName);

//        Day 3: Pass 3: Using ViewHolder to get textview and imageview
        ViewHolder vh = (ViewHolder) mainView.getTag();

//        display the current name on the textview
        vh.textview.setText((countryName));

//        Day 3: Pass 4: Using adapter to get different images for different countries
        AssetManager manager = context.getAssets();
        String filePath = "flags-32/" + countryName + ".png";
        try {
//            Day 3: Pass 4: Implementing getting image from cache.
            Bitmap image = cache.get(filePath);
            if (image == null) {
                Log.i(TAG, "Didn't hit cache for - " + filePath);
                InputStream inStr = manager.open(filePath);
                image = BitmapFactory.decodeStream(inStr);
                cache.put(filePath, image);
            }

            vh.imageView.setImageBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
//            set the default flag in case no proper country flag is found
            vh.imageView.setImageResource(R.drawable.india_flag);
        }

        return mainView;
    }
}
