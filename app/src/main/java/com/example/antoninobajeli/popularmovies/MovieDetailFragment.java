package com.example.antoninobajeli.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Rating;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.antoninobajeli.popularmovies.settings.SettingsActivity;
import com.example.antoninobajeli.popularmovies.utils.MovieNetworkUtils;
import com.example.antoninobajeli.popularmovies.utils.MoviesContent;
import com.example.antoninobajeli.popularmovies.utils.MoviesJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;

import static android.R.attr.id;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    View rootView;
    static String LOG_TAG = MovieDetailFragment.class.getSimpleName();


    private MoviesContent.MovieItem mItem;
    private Button videoComp;


    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate");

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = MoviesContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(mItem.overview);

            ImageView imgPosterDetail=(ImageView) rootView.findViewById(R.id.detailposter);
            String imgLink=getString(R.string.img_base_path)+getString(R.string.poster_image_size)+mItem.path;
            //videos
            Log.d(LOG_TAG,imgLink);
            Picasso.with(imgPosterDetail.getContext()).load(imgLink).into(imgPosterDetail);
            ((RatingBar) rootView.findViewById(R.id.rating)).setRating(Float.parseFloat(mItem.voteAverage));
            ((TextView) rootView.findViewById(R.id.rating_value)).setText(mItem.voteAverage+"/10");





            ((TextView) rootView.findViewById(R.id.releasedate)).setText(mItem.releaseDate);
            ((TextView) rootView.findViewById(R.id.vote_cnt)).setText(mItem.voteCnt);

            ((TextView) rootView.findViewById(R.id.popularity)).setText(mItem.popularity);

            videoComp=((Button) rootView.findViewById(R.id.videoButt));


            videoComp.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 watchYoutubeVideo(view.getTag().toString());

                                             }
                                         }
            );




            loadVideosData(mItem.movieId);


        }

        return rootView;
    }


    public void watchYoutubeVideo(String key){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(" https://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }


    /**
     *
     * This method calls the Task tha load data from internet
     *
     */
    private void loadVideosData(String movieId) {
        if (isOnline()==true){
            Log.d(LOG_TAG,"Loading videos for movieId: "+movieId);
            new FetchMovieVideosTask().execute(movieId);

        }else{
            //showErrorMessage();
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }





    /**
     * Retrieves the data from Internet as String
     * that pass
     */

    public class FetchMovieVideosTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            //mProgProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String movieId = params[0];
            URL videosRequestUrl = MovieNetworkUtils.buildVideoListUrl(Integer.parseInt(movieId), BuildConfig.THE_MOVIE_DB_API_TOKEN);

            try {
                String jsonVideosResponse = MovieNetworkUtils
                        .getResponseFromHttpUrl(videosRequestUrl);

                String[] videosFromJsonData = MoviesJsonUtils
                        .getVideosContentFromJson(getContext(),
                                jsonVideosResponse);






                return videosFromJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviesData) {
            //mProgProgressBar.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                int i=0;
                MoviesContent.clearVideoItems();
                for (String movieString : moviesData) {
                    i++;
                    MoviesContent.VideoItem it=MoviesJsonUtils.convertToVideoObj(i,movieString);

                    MoviesContent.addVideoItem(it);
                    videoComp.setTag(it.key);
                    videoComp.setText(it.name);

                }
                //showMovieDataView();
            }else{
                //showErrorMessage();
            }

        }
    }











}
