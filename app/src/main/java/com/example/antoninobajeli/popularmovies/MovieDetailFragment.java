package com.example.antoninobajeli.popularmovies;

import android.app.Activity;
import android.media.Rating;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.antoninobajeli.popularmovies.utils.MoviesContent;
import com.squareup.picasso.Picasso;



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
    static String LOG_TAG = MovieDetailFragment.class.getSimpleName();


    private MoviesContent.MovieItem mItem;


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
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_synopsis)).setText(mItem.overview);

            ImageView imgPosterDetail=(ImageView) rootView.findViewById(R.id.detailposter);
            String imgLink=getString(R.string.img_base_path)+getString(R.string.poster_image_size)+mItem.path;
            Log.d(LOG_TAG,imgLink);
            Picasso.with(imgPosterDetail.getContext()).load(imgLink).into(imgPosterDetail);
            ((RatingBar) rootView.findViewById(R.id.rating)).setRating(Float.parseFloat(mItem.voteAverage));
            ((TextView) rootView.findViewById(R.id.rating_value)).setText(mItem.voteAverage+"/10");


            ((TextView) rootView.findViewById(R.id.releasedate)).setText(mItem.releaseDate);
            ((TextView) rootView.findViewById(R.id.vote_cnt)).setText(mItem.voteCnt);

            ((TextView) rootView.findViewById(R.id.popularity)).setText(mItem.popularity);

        }

        return rootView;
    }
}
