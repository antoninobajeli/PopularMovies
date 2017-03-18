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
    final static String PM_MOVIE_ID = "id";
    final static String PM_ORIGINAL_TITLE = "original_title";
    final static String PM_RELEASE_DATE = "release_date";
    final static String PM_OVERVIEW = "overview";
    final static String PM_POPULARITY = "popularity";
    final static String PM_VOTE_COUNT = "vote_count";
    final static String PM_VOTE_AVERAGE = "vote_average";
    final static String PM_POSTER_PATH = "poster_path";

    final static String VL_VIDEO_ID="id";
    final static String VL_LANGUAGE="iso_639_1";
    final static String VL_COUNTRY="iso_3166_1";
    final static String VL_KEY="key";
    final static String VL_NAME="name";
    final static String VL_SITE="site";
    final static String VL_SIZE="size";
    final static String VL_TYPE="type";


    final static String RV_ID="id";
    final static String RV_AUTHOR="author";
    final static String RV_CONTENT="content";
    final static String RV_URL="url";



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

            String movieId=movieJSObject.getString(PM_MOVIE_ID);
            String title = movieJSObject.getString(PM_ORIGINAL_TITLE);
            String releaseDate = movieJSObject.getString(PM_RELEASE_DATE);
            String popularity = movieJSObject.getString(PM_POPULARITY);
            String voteCnt = movieJSObject.getString(PM_VOTE_COUNT);
            String voteAverage = movieJSObject.getString(PM_VOTE_AVERAGE);
            String overview = movieJSObject.getString(PM_OVERVIEW);

            String posterPath = movieJSObject.getString(PM_POSTER_PATH);
            return new MoviesContent.MovieItem(Integer.toString(id),movieId,title,releaseDate,overview,posterPath,popularity,voteAverage,voteCnt);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Error converting Json to movie Item");
            e.printStackTrace();
        }
        return null;
    }







    /**
     *
     * @param context
     * @param videoJsonStr
     * @return
     * @throws JSONException
     */

    public static String[] getVideosContentFromJson(Context context, String videoJsonStr)
            throws JSONException {

        /* Movie information. Each movie's info is an element of the "list" array */
        final String PM_LIST = "results";
        final String OWM_MESSAGE_CODE = "cod";

        /* MoviesContent array to hold each movie */
        String[] parsedVideosData = null;

        JSONObject videosJson = new JSONObject(videoJsonStr);

        // TODO
        /* Is there an error? */
        if (videosJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = videosJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray videosJSArray = videosJson.getJSONArray(PM_LIST);


        parsedVideosData = new String[videosJSArray.length()];

        for (int i = 0; i < videosJSArray.length(); i++) {
            /* Get the JSON object representing the movie */
            parsedVideosData[i]=videosJSArray.getJSONObject(i).toString();

            Log.d(LOG_TAG,parsedVideosData[i]);
        }
        return parsedVideosData;
    }


    static public VideosContent.VideoItem convertToVideoObj(int id,String jsonNodeStr){
        try {

            /*
            {"id":135397,
                    "results":[
                            {"id":"54749bea9251414f41001b58","iso_639_1":"en","iso_3166_1":"US","key":"bvu-zlR5A8Q","name":"Teaser","site":"YouTube","size":1080,"type":"Teaser"},
                            {"id":"5474d2339251416e58002ae1","iso_639_1":"en","iso_3166_1":"US","key":"RFinNxS5KN4","name":"Official Trailer","site":"YouTube","size":1080,"type":"Trailer"},
                            {"id":"56404e639251417052000369","iso_639_1":"en","iso_3166_1":"US","key":"aJJrkyHas78","name":"Official Global Trailer","site":"YouTube","size":1080,"type":"Trailer"}]}
            */
            JSONObject videoJSObject = new JSONObject(jsonNodeStr);

            String videoId=videoJSObject.getString(VL_VIDEO_ID);
            String language = videoJSObject.getString(VL_LANGUAGE);
            String country = videoJSObject.getString(VL_COUNTRY);
            String key = videoJSObject.getString(VL_KEY);
            String name = videoJSObject.getString(VL_NAME);
            String site = videoJSObject.getString(VL_SITE);
            String size = videoJSObject.getString(VL_SIZE);
            String type = videoJSObject.getString(VL_TYPE);

            return new VideosContent.VideoItem(videoId,language,country,key,name,site,size,type);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Error converting Json to video Item");
            e.printStackTrace();
        }
        return null;
    }












    /**
     *
     * @param context
     * @param reviewsJsonStr
     * @return
     * @throws JSONException
     */

    public static String[] getReviewsContentFromJson(Context context, String reviewsJsonStr)
            throws JSONException {

        /* Movie information. Each movie's info is an element of the "list" array */
        final String PM_LIST = "results";
        final String OWM_MESSAGE_CODE = "cod";

        /* MoviesContent array to hold each movie */
        String[] parsedReviewsData = null;

        JSONObject reviewsJson = new JSONObject(reviewsJsonStr);

        // TODO
        /* Is there an error? */
        if (reviewsJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = reviewsJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray videosJSArray = reviewsJson.getJSONArray(PM_LIST);


        parsedReviewsData = new String[videosJSArray.length()];

        for (int i = 0; i < videosJSArray.length(); i++) {
            /* Get the JSON object representing the movie */
            parsedReviewsData[i]=videosJSArray.getJSONObject(i).toString();

            Log.d(LOG_TAG,parsedReviewsData[i]);
        }
        return parsedReviewsData;
    }


    static public ReviewsContent.ReviewItem convertToReviewObj(int id,String jsonNodeStr){
        try {

            /*
            "results": [
                        {
                          "id": "55910381c3a36807f900065d",
                          "author": "jonlikesmoviesthatdontsuck",
                          "content": "I was a huge fan of the original 3 movies, they were out when I was younger, and I grew up loving dinosaurs because of them. This movie was awesome, and I think it can stand as a testimonial piece towards the capabilities that Christopher Pratt has. He nailed it. The graphics were awesome, the supporting cast did great and the t rex saved the child in me. 10\\5 stars, four thumbs up, and I hope that star wars episode VII doesn't disappoint,",
                          "url": "https://www.themoviedb.org/review/55910381c3a36807f900065d"
                        },
            */
            JSONObject reviewJSObject = new JSONObject(jsonNodeStr);

            String reviewId=reviewJSObject.getString(RV_ID);
            String author = reviewJSObject.getString(RV_AUTHOR);
            String content = reviewJSObject.getString(RV_CONTENT);
            String url = reviewJSObject.getString(RV_URL);

            return new ReviewsContent.ReviewItem(reviewId,author,content,url);
        } catch (JSONException e) {
            Log.e(LOG_TAG,"Error converting Json to video Item");
            e.printStackTrace();
        }
        return null;
    }



}
