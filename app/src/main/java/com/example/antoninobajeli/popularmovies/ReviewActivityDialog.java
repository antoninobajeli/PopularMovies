package com.example.antoninobajeli.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.antoninobajeli.popularmovies.utils.MovieNetworkUtils;
import com.example.antoninobajeli.popularmovies.utils.MoviesContent;
import com.example.antoninobajeli.popularmovies.utils.MoviesJsonUtils;
import com.example.antoninobajeli.popularmovies.utils.ReviewsContent;

import java.net.URL;
import java.util.List;

public class ReviewActivityDialog extends AppCompatActivity {
    static String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    ReviewActivityDialog.ReviewsSimpleItemRecyclerViewAdapter reviewsAdapter;
    View reviewsRecyclerView;
    public static final String ARG_ITEM_ID = "item_id";
    private MoviesContent.MovieItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_dialog);

        Bundle b = getIntent().getExtras();
        if(b != null){
            mItem = MoviesContent.ITEM_MAP.get(b.getString(ARG_ITEM_ID));
        }


        reviewsRecyclerView = findViewById(R.id.reviews_list);
        setupReviewsRecyclerView((RecyclerView) reviewsRecyclerView, getApplicationContext());
        loadReviewsData(mItem.movieId);
        setTitle(R.string.review_dialog_title + mItem.title);
    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     *
     * This method calls the Task tha load data from internet
     *
     */
    private void loadReviewsData(String movieId) {
        if (isOnline()==true){
            Log.d(LOG_TAG,"Loading reviews for movieId: "+movieId);
            new FetchMovieReviewsTask().execute(movieId);

        }else{
            //showErrorMessage();
        }
    }


    private void setupReviewsRecyclerView(@NonNull RecyclerView recyclerView, Context context) {
        // Define a layout manager for RecyclerView

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        reviewsAdapter=new ReviewActivityDialog.ReviewsSimpleItemRecyclerViewAdapter(ReviewsContent.REVIEW_ITEMS);

        recyclerView.setAdapter(reviewsAdapter);
    }


    public class ReviewsSimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ReviewActivityDialog.ReviewsSimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ReviewsContent.ReviewItem> mValues;

        public ReviewsSimpleItemRecyclerViewAdapter(List<ReviewsContent.ReviewItem> items) {
            mValues = items;
        }

        @Override
        public ReviewsSimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reviews_list_content, parent, false);
            return new ReviewsSimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReviewActivityDialog.ReviewsSimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

            holder.mItem = mValues.get(position);
            holder.mTextViewAuthor.setText(holder.mItem.author);
            holder.mTextViewBody.setText(holder.mItem.content);
            holder.mTextViewUrl.setText(holder.mItem.url.substring(0,20));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTextViewAuthor;
            public final TextView mTextViewBody;
            public final TextView mTextViewUrl;

            public ReviewsContent.ReviewItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextViewAuthor = (TextView) view.findViewById(R.id.tvReviewAuthor);
                mTextViewBody = (TextView) view.findViewById(R.id.tvReviewBody);
                mTextViewUrl = (TextView) view.findViewById(R.id.tvReviewUrl);
                Log.d(LOG_TAG,"mTextViewUrl"+mTextViewUrl);
            }

            @Override
            public String toString() {
                return super.toString() +  mItem.author;
            }
        }
    }




    public class FetchMovieReviewsTask extends AsyncTask<String, Void, String[]> {

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
            URL reviewsRequestUrl = MovieNetworkUtils.buildReviewListUrl(Integer.parseInt(movieId), BuildConfig.THE_MOVIE_DB_API_TOKEN);

            try {
                String jsonReviewsResponse = MovieNetworkUtils
                        .getResponseFromHttpUrl(reviewsRequestUrl);

                String[] reviewsFromJsonData = MoviesJsonUtils
                        .getReviewsContentFromJson(getApplicationContext(),
                                jsonReviewsResponse);



                return reviewsFromJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] reviewsData) {
            if (reviewsData != null) {
                int i=0;
                ReviewsContent.clearReviewItems();
                for (String reviewString : reviewsData) {
                    i++;
                    ReviewsContent.ReviewItem rit=MoviesJsonUtils.convertToReviewObj(i,reviewString);
                    ReviewsContent.addReviewItem(rit);
                }
                reviewsAdapter.notifyDataSetChanged();
            }else{
                Log.d(LOG_TAG,"Reviews data is null");
            }

        }
    }
}
