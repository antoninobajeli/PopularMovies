package com.example.antoninobajeli.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.antoninobajeli.popularmovies.settings.SettingsActivity;
import com.example.antoninobajeli.popularmovies.utils.MovieNetworkUtils;
import com.example.antoninobajeli.popularmovies.utils.MoviesJsonUtils;
import com.squareup.picasso.Picasso;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antoninobajeli.popularmovies.utils.MoviesContent;

import java.net.URL;
import java.util.List;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {
    static String LOG_TAG = MovieListActivity.class.getSimpleName();

    // Main Activity

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane=false;
    private boolean mPhoneLand=false;
    private int mGridColumns=2;
    private ProgressBar mProgProgressBar;
    private TextView mErrorMessageTextView;
    private final String TOP_RATED_QUERY_KEY = "top_rated";
    private final String MOST_POPULAR_QUERY_KEY  ="popular";


    View recyclerView;
    SimpleItemRecyclerViewAdapter av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate");

        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.movie_list);
        mErrorMessageTextView=(TextView) findViewById(R.id.tv_error_message);
        mProgProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, getApplicationContext());


        loadMoviesData();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView,Context context) {
        // Define a layout manager for RecyclerView

        GridLayoutManager mLayoutManager = new GridLayoutManager(context,calculateNoOfColumns(context));
        recyclerView.setLayoutManager(mLayoutManager);
        av=new SimpleItemRecyclerViewAdapter(MoviesContent.MOVIE_ITEMS);
        recyclerView.setAdapter(av);
    }




    /**
     *
     * This method calls the Task tha load data from internet
     *
     */
    private void loadMoviesData() {
        if (isOnline()==true){
            showMovieDataView();

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean sortPrefIsPopular=sharedPrefs.getBoolean(SettingsActivity.MOVIE_SORT_TYPE_PREF_KEY,true);
            Log.d(LOG_TAG,"shared sortPrefIsPopular "+sortPrefIsPopular);
            if (sortPrefIsPopular){
                new FetchMovieTask().execute(MOST_POPULAR_QUERY_KEY);
            }else{
                new FetchMovieTask().execute(TOP_RATED_QUERY_KEY);
            }


        }else{
            showErrorMessage();
        }
    }

    private void showMovieDataView(){
        av.notifyDataSetChanged();
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),R.string.error_data_message,Toast.LENGTH_LONG).show();
    }




    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<MoviesContent.MovieItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<MoviesContent.MovieItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            String imgLink=getString(R.string.img_base_path)+getString(R.string.thumb_image_size)+holder.mItem.path;
            Log.d(LOG_TAG,imgLink);
            Picasso.with(holder.mPosterView.getContext()).load(imgLink).into(holder.mPosterView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mPosterView;
            public MoviesContent.MovieItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPosterView = (ImageView) view.findViewById(R.id.poster);

            }

            @Override
            public String toString() {
                return super.toString() +  mItem.title;
            }
        }
    }


    /**
     * Retrieves the data from Internet as String
     * that pass jsonMoviesResponse String data to MoviesJsonUtils to convert
     * data from Json String to Array of Strings
     * On Post Execute
     */

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            mProgProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortType = params[0];
            URL moviesRequestUrl = MovieNetworkUtils.buildUrl(sortType, BuildConfig.THE_MOVIE_DB_API_TOKEN);

            try {
                String jsonMoviesResponse = MovieNetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                String[] moviesFromJsonData = MoviesJsonUtils
                        .getMovieContentFromJson(MovieListActivity.this,
                                jsonMoviesResponse);






                return moviesFromJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviesData) {
            mProgProgressBar.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                int i=0;
                MoviesContent.clearMovieItems();
                for (String movieString : moviesData) {
                    i++;
                    MoviesContent.MovieItem it=MoviesJsonUtils.convertToMovieObj(i,movieString);

                    MoviesContent.addMovieItem(it);
                }
                showMovieDataView();
            }else{
                showErrorMessage();
            }

        }
    }






    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movies, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadMoviesData();
            return true;
        }
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
