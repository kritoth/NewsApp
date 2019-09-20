package com.example.bf8ezm.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.bf8ezm.newsapp.MainActivity.LOG_TAG;

/**
 * Created by BF8EZM on 2017. 07. 19..
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Query URL */
    private String mUrl;

    // Constructor
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading is called...");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null) {
            return null;
        }
        Log.i(LOG_TAG, "TEST: loadInBackground is called...");
        // Perform the network request, parse the response, and extract a list of news.
        List<News> newses = QueryUtils.fetchNewsData(mUrl);
        return newses;
    }
}
