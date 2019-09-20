package com.example.bf8ezm.newsapp;

/**
 * Created by BF8EZM on 2017. 07. 19..
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.bf8ezm.newsapp.MainActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving news data from The Guardian.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL to the fetchNewsData method at the bottom
     * That is why this is private
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     * to the fetchNewsData method at the bottom
     * That is why this is private
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     * Returns the toString to the fetchNewsData method at the bottom
     * That is why this is private
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from parsing a JSON response
     * to the fetchNewsData method at the bottom.
     * That is why this is private
     */
    private static ArrayList<News> extractNews (String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> newses = new ArrayList<>();

        // Try to parse the NEWS_REQUEST_URL. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // To build up a list of News objects with the corresponding data the following need to be done

            //1st create the JSONObject from the response we got from the web
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //2nd on this object we call the getArray because it contains an array
            JSONObject newsObject = baseJsonResponse.getJSONObject("response");

            //3rd on this object we call the getArray because it contains an array
            JSONArray newsArray = newsObject.getJSONArray("results");

            //4th on this array we have to loop this array for finding the objects within
            //and the objects will be further examined to find the needed strings inside them
            for (int i=0; i<newsArray.length(); i++) {
                //The found objects are stored into a variable
                JSONObject currentNews = newsArray.getJSONObject(i);

                // On this variable we run the getters for the wanted final values with the JSON tags as inputs
                String title;
                String section;
                // 1st Extract the value for the key called "webTitle"
                if (currentNews.has("webTitle")) {
                    // parse the webTitle field
                    title = currentNews.getString("webTitle");
                } else {
                    // Placeholder text (e.g. "webTitle N/A")
                    title = "Web Title not exists";
                };
                // 2nd Extract the value for the key called "sectionName"
                if (currentNews.has("sectionName")) {
                    // parse the sectionName field
                    section = currentNews.getString("sectionName");
                } else {
                    // Placeholder text (e.g. "sectionName N/A")
                    section = "Section name not exists";
                };

                // Extract the value for the key called "webUrl"
                String url = currentNews.getString("webUrl");

                //Now we can create a final object with the needed values in it
                News news = new News(title, section, url);

                // Add the new {@link News} to the list of news.
                newses.add(news);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return newses;
    }

    /**
     * Query the Guardian's dataset from the above private method and return a list of {@link News} objects.
     * So this is public and can be accessed by the MainActivity class.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        Log.i(LOG_TAG, "TEST: fetchNewsData is called...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}
        List<News> newses = extractNews(jsonResponse);

        // Return the list of {@link News}s
        return newses;
    }
}
