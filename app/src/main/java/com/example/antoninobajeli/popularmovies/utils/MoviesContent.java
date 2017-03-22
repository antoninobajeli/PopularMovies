package com.example.antoninobajeli.popularmovies.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by antoninobajeli on 18/03/17.
 */
public class MoviesContent {
    private static final String LOG_TAG=MoviesContent.class.getSimpleName();
    /**
     * An array of items.
     */
    public static final List<MovieItem> MOVIE_ITEMS = new ArrayList<MovieItem>();

    /**
     * A map of items, by ID.
     */
    public static final Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    public static void clearMovieItems(){
        MOVIE_ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addMovieItem(MovieItem item) {
        MOVIE_ITEMS.add(item); //used by recycler view
        ITEM_MAP.put(item.id, item);
    }


    /*public static MovieItem createMovieItem(int position,String title) {
        String moviejson="{\"poster_path\":\"\\/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg\",\"adult\":false,\"overview\":\"The quiet life of a terrier named Max is upended when his owner takes in Duke, a stray whom Max instantly dislikes.\",\"release_date\":\"2016-06-18\",\"genre_ids\":[12,16,35,10751],\"id\":328111,\"original_title\":\"The Secret Life of Pets\",\"original_language\":\"en\",\"title\":\"The Secret Life of Pets\",\"backdrop_path\":\"\\/lubzBMQLLmG88CLQ4F3TxZr2Q7N.jpg\",\"popularity\":149.543296,\"vote_count\":1983,\"video\":false,\"vote_average\":5.8}";

        http://image.tmdb.org/t/p/w185



        return  new MovieItem(String.valueOf(position), position+ " "+title, makeDetails(position));

    }*/
/*
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * Item representing Movie content.
     */
    public static class MovieItem {
        public final String id;
        public final String movieId;
        public final String title;
        public final String releaseDate;
        public final String overview;
        public final String path;
        public final String popularity;
        public final String voteAverage;
        public final String voteCnt;


        public MovieItem(String id,
                         String movieId,
                         String title,
                         String releaseDate,
                         String overview,
                         String path,
                         String popularity,
                         String voteAverage,
                         String voteCnt
                         ) {
            this.id = id;
            this.movieId=movieId;
            this.title = title;
            this.releaseDate=releaseDate;
            this.overview = overview;
            this.path=path;
            this.popularity= (popularity != null)? popularity.substring(0,popularity.indexOf(".")) : null;

            this.voteAverage = voteAverage;
            this.voteCnt = voteCnt;

        }

        @Override
        public String toString() {
            return title;
        }
    }


}
