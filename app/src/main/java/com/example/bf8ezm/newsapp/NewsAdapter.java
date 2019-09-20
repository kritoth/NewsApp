package com.example.bf8ezm.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by BF8EZM on 2017. 07. 19..
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**Constructor, creates a new NewsAdapter object with 2 params
     *
     * @param context The current context. Used to inflate the layout file.
     * @param newses A List of News objects to display in a list
     */
    public NewsAdapter(Context context, ArrayList<News> newses) {
        super(context, 0, newses);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        // Get the title from the currentNews object and set this text on
        // the title TextView.
        titleTextView.setText(currentNews.getTitle());

        // Find the TextView in the list_item.xml layout with the ID section
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        // Get the section from the currentBook object and set this text on
        // the section TextView.
        sectionTextView.setText(currentNews.getSection());

        // Return the whole list_item layout (containing 2 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
