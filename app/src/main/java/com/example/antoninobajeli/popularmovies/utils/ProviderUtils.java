package com.example.antoninobajeli.popularmovies.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.antoninobajeli.popularmovies.data.PopularmovieContract;

/**
 * Created by antoninobajeli on 19/03/17.
 */

public class ProviderUtils {

    static String LOG_TAG = ProviderUtils.class.getSimpleName();

    /**
     *
     * @param ctx
     * @param movieTitle
     * @param movieId
     * @return
     */
    public static String addNewPreferredMovie(Context ctx,String movieTitle, String movieId) {

        // Insert new preferred movie data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the movie info into the ContentValues
        contentValues.put(PopularmovieContract.PopularmovieEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(PopularmovieContract.PopularmovieEntry.COLUMN_MOVIE_TITLE, movieTitle);

        // Insert the content values via a ContentResolver
        Uri uri = ctx.getContentResolver()
                .insert(PopularmovieContract.PopularmovieEntry.CONTENT_URI, contentValues);
        if(uri != null) {
            Log.d(LOG_TAG,"movie added to local db "+uri.toString());
        }else{
            Log.d(LOG_TAG,"problem adding preferred movie to local db !!");
        }
        return uri.toString();

    }


    /**
     *
     * @param ctx
     * @param movieId
     * @return
     */
    public static boolean checkStarredMovie(Context ctx, String movieId) {

        // Build appropriate uri with String row id appended
        Uri uri = PopularmovieContract.PopularmovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();
        Log.d(LOG_TAG,"query "+uri.toString());

        // query a single row of data using a ContentResolver
        Cursor c=ctx.getContentResolver()
                .query(uri, null, PopularmovieContract.PopularmovieEntry.COLUMN_MOVIE_ID+"=?", new String[]{movieId},null,null);

        if (c.getCount()>0)
            return true;
        else
            return false;
    }


    /**
     *
     * @param ctx
     * @param movieId
     * @return
     */
    public static int deleteStarredMovie(Context ctx, String movieId) {
        int deleted=0;
        // Build appropriate uri with String row id appended

        Uri uri = PopularmovieContract.PopularmovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId).build();

        // Delete a single row of data using a ContentResolver
        return ctx.getContentResolver().delete(uri, null, null);
    }
}
