package com.example.bf8ezm.newsapp;

/**
 * Created by BF8EZM on 2017. 07. 19..
 */

public class News {

    // Title
    private String mTitle;
    // Section
    private String mSection;
    // url
    private String mUrl;

    // Constructor, creates a new News object with 2 params
    public News (String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    public String getTitle (){
        return mTitle;
    }

    public String getSection () {
        return mSection;
    }

    public String getUrl () {
        return mUrl;
    }
}
