package com.flipkart.countryinfoapp;


import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class WikiFragment extends Fragment {

    private WebView webView;
    private static String WIKI_URL = "http://en.wikipedia.org/wiki/";

    public WikiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_wiki, container, false);
        webView = (WebView) fragView.findViewById(R.id.webView);

//        Day 3: Pass 5 : originally, webview was launching browser activity to open the link. Now
//        We are going to make it launch inside webview. Before, on click back, we were coming to an
//        empty WikiFragment
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//      get access to arguments that were passed to this fragment
        Bundle bundle = getArguments();
//        extract the country name from
        String countryName = bundle.getString("COUNTRY_NAME");
        String countryWikiPath = WIKI_URL + countryName;

//        load url in webview
        webView.loadUrl(countryWikiPath);

        return fragView;
    }


}
