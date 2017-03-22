package com.example.antoninobajeli.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.antoninobajeli.popularmovies.data.PopularmovieContract.*;
/**
 * Created by antoninobajeli on 18/03/17.
 */


public class PopularmovieDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "Popularmovie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public PopularmovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold Popularmovie data
        final String SQL_CREATE_Popularmovie_TABLE = "CREATE TABLE " + PopularmovieEntry.TABLE_NAME + " (" +
                PopularmovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PopularmovieEntry.COLUMN_MOVIE_ID + " INTEGER KEY, " +
                PopularmovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_Popularmovie_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularmovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }






}
