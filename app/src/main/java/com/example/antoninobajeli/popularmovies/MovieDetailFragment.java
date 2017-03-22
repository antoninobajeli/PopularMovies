package com.example.antoninobajeli.popularmovies;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.antoninobajeli.popularmovies.data.PopularmovieDbHelper;
import com.example.antoninobajeli.popularmovies.utils.ProviderUtils;
import com.example.antoninobajeli.popularmovies.utils.MovieNetworkUtils;
import com.example.antoninobajeli.popularmovies.utils.MoviesContent;
import com.example.antoninobajeli.popularmovies.utils.MoviesJsonUtils;
import com.example.antoninobajeli.popularmovies.utils.VideosContent;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment  {

    static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    View rootView;

    View videosRecyclerView;
    MovieDetailFragment.VideosSimpleItemRecyclerViewAdapter videosAdapter;




    private MoviesContent.MovieItem mItem;
    private Button goReviewsBtn;
    private SQLiteDatabase mDb;
    private boolean mTwoPane=false;
    ImageButton preferredImButton,shareImButton;
    Intent intentReviews;
    String argItemIdVal;

    String mMainTrailerLink=null;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PopularmovieDbHelper dbHelper = new PopularmovieDbHelper(this.getContext());
        mDb = dbHelper.getReadableDatabase();


        Log.d(LOG_TAG,"onCreate");

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            argItemIdVal =getArguments().getString(ARG_ITEM_ID);
            mItem = MoviesContent.ITEM_MAP.get(argItemIdVal);

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




            preferredImButton = ((ImageButton) rootView.findViewById(R.id.preferredImageButton));

            if (ProviderUtils.checkStarredMovie(getContext(),mItem.movieId)){
                preferredImButton.setImageResource(btn_star_big_on);
            }else {
                preferredImButton.setImageResource(btn_star_big_off);
            }

            preferredImButton.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ProviderUtils.checkStarredMovie(getContext(),mItem.movieId)){
                                int deleted = ProviderUtils.deleteStarredMovie(getContext(),mItem.movieId);
                                preferredImButton.setImageResource(btn_star_big_off);
                                Log.d(LOG_TAG,"deleted "+deleted);

                            }else {
                                String uriRef= ProviderUtils.addNewPreferredMovie(getContext(),mItem.title,mItem.movieId);
                                Log.d(LOG_TAG,"Uri of inserted preferrend movie"+uriRef);
                                preferredImButton.setImageResource(btn_star_big_on);

                            }

                        }
                    }
            );

            shareImButton = ((ImageButton) rootView.findViewById(R.id.sharingImageButton));

            shareImButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mMainTrailerLink != null){
                                shareMainYoutubeVideo();
                            }
                        }
                    }
            );




            ((TextView) rootView.findViewById(R.id.releasedate)).setText(mItem.releaseDate);
            ((TextView) rootView.findViewById(R.id.vote_cnt)).setText(mItem.voteCnt);

            ((TextView) rootView.findViewById(R.id.popularity)).setText(mItem.popularity);


            goReviewsBtn=((Button) rootView.findViewById(R.id.goToReviews));

            intentReviews = new Intent(getActivity(), ReviewActivityDialog.class);

            goReviewsBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {

                                                 Bundle bund = new Bundle();
                                                 bund.putString(ARG_ITEM_ID, argItemIdVal); //Your id
                                                 intentReviews.putExtras(bund); //passing the extra data with MovieID to the reviewsActivity
                                                 startActivity(intentReviews);
                                             }
                                         }
            );


            videosRecyclerView = rootView.findViewById(R.id.videos_list);

            setupVideosRecyclerView((RecyclerView) videosRecyclerView, getContext());
            loadVideosData(mItem.movieId);
        }

        return rootView;
    }





    public void shareMainYoutubeVideo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mMainTrailerLink);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Movie trailar"));
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

    private void setupVideosRecyclerView(@NonNull RecyclerView recyclerView, Context context) {
        // Define a layout manager for RecyclerView

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        videosAdapter=new MovieDetailFragment.VideosSimpleItemRecyclerViewAdapter(VideosContent.VIDEO_ITEMS);

        recyclerView.setAdapter(videosAdapter);
    }




    public class VideosSimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieDetailFragment.VideosSimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<VideosContent.VideoItem> mValues;

        public VideosSimpleItemRecyclerViewAdapter(List<VideosContent.VideoItem> items) {
            mValues = items;
        }

        @Override
        public VideosSimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.videos_list_content, parent, false);
            return new VideosSimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MovieDetailFragment.VideosSimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.mButtonView.setTag(holder.mItem.key);
            holder.mButtonView.setText(holder.mItem.name);
            holder.mButtonView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            watchYoutubeVideo(view.getTag().toString());
                        }
                    }
            );


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final Button mButtonView;
            public VideosContent.VideoItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mButtonView = (Button) view.findViewById(R.id.videoButt);
                Log.d(LOG_TAG,"mButtonView"+mButtonView);

            }

            @Override
            public String toString() {
                return super.toString() +  mItem.name;
            }
        }
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
            mMainTrailerLink=null;
            shareImButton.setEnabled(false);
            if (moviesData != null) {
                int i=0;
                VideosContent.clearVideoItems();
                for (String movieString : moviesData) {
                    i++;

                    VideosContent.VideoItem it=MoviesJsonUtils.convertToVideoObj(i,movieString);
                    VideosContent.addVideoItem(it);
                    if (it!=null && i==1){
                      mMainTrailerLink="https://www.youtube.com/watch?v=" + it.videoId;
                      shareImButton.setEnabled(true);
                    }
                }
                videosAdapter.notifyDataSetChanged();
            }else{
                //showErrorMessage();
            }

        }
    }














}
