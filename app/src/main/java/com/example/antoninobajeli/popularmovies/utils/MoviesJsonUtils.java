package com.example.antoninobajeli.popularmovies.utils;

/**
 * Created by antoninobajeli on 03/02/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MoviesJsonUtils {
    private static final String LOG_TAG = MoviesJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

    /* All data related to JSON  "movie" object */
    final static String PM_ADULT = "adult";
    final static String PM_ORIGINAL_TITLE = "original_title";
    final static String PM_RELEASE_DATE = "release_date";
    final static String PM_OVERVIEW = "overview";
    final static String PM_POPULARITY = "popularity";
    final static String PM_VOTE_COUNT = "vote_count";
    final static String PM_VOTE_AVERAGE = "vote_average";
    final static String PM_POSTER_PATH = "poster_path";



    public static String[] getMovieContentFromJson(Context context, String movieJsonStr)
            throws JSONException {

        /* Movie information. Each movie's info is an element of the "list" array */
        final String PM_LIST = "results";
        final String OWM_MESSAGE_CODE = "cod";

        /* MoviesContent array to hold each movie */
        String[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(movieJsonStr);

        // TODO
        /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieJSArray = moviesJson.getJSONArray(PM_LIST);
        parsedMovieData = new String[movieJSArray.length()];

        for (int i = 0; i < movieJSArray.length(); i++) {
            /* Get the JSON object representing the movie */
            parsedMovieData[i]=movieJSArray.getJSONObject(i).toString();

            Log.d(LOG_TAG,parsedMovieData[i]);
        }
        return parsedMovieData;
    }


    static public MoviesContent.MovieItem convertToMovieObj(int id,String jsonNodeStr){
        try {
            JSONObject movieJSObject = new JSONObject(jsonNodeStr);

            String title = movieJSObject.getString(PM_ORIGINAL_TITLE);
            String releaseDate = movieJSObject.getString(PM_RELEASE_DATE);
            String popularity = movieJSObject.getString(PM_POPULARITY);
            String voteCnt = movieJSObject.getString(PM_VOTE_COUNT);
            String voteAverage = movieJSObject.getString(PM_VOTE_AVERAGE);
            String overview = movieJSObject.getString(PM_OVERVIEW);

            String posterPath = movieJSObject.getString(PM_POSTER_PATH);
            return new MoviesContent.MovieItem(Integer.toString(id),title,releaseDate,overview,posterPath,popularity,voteAverage,voteCnt);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Error converting Json to movie Item");
            e.printStackTrace();
        }
        return null;
    }


}
