package com.example.antoninobajeli.popularmovies.utils;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.antoninobajeli.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by antoninobajeli on 03/02/17.
 */
public final class MovieNetworkUtils {
    private static final String LOG_TAG = MovieNetworkUtils.class.getSimpleName();
    private static final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie";
    private static final String API_KEY ="api_key";


    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param sortType The kinde of sort that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String sortType,String API_KEY_VAL) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL+"/"+sortType+"/").buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VAL)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "URI has been Built" + url);

        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}