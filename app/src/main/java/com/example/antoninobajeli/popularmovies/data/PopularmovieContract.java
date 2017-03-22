package com.example.antoninobajeli.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by antoninobajeli on 18/03/17.
 */

public class PopularmovieContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.antoninobajeli.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "movies";



    public static final class PopularmovieEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String TABLE_NAME = "popularmovie";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieId";


        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        tasks
         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    movieTitle     |    movieId   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |  The park   |       1342      |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |    Sweet moments     |       2343       |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |   the urricane     |       222     |
         - - - - - - - - - - - - - - - - - - - - - -

         */
    }

}