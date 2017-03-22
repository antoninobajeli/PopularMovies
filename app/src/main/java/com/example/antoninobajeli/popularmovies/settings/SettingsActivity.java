package com.example.antoninobajeli.popularmovies.settings;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.antoninobajeli.popularmovies.R;

public class SettingsActivity extends AppCompatActivity {
    static String LOG_TAG = SettingsActivity.class.getSimpleName();
    public static final String MOVIE_SORT_TYPE_PREF_KEY="movie_sort_type";
    public static final String MOVIE_SHOW_TYPE_PREF_KEY="movie_show_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"created");
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG,"onOptionsItemSelected");
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


}