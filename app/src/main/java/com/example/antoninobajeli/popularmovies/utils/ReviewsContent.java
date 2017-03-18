package com.example.antoninobajeli.popularmovies.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoninobajeli on 18/03/17.
 */

public class ReviewsContent {
    private static final String LOG_TAG=ReviewsContent.class.getSimpleName();

    public static final List<ReviewItem> REVIEW_ITEMS = new ArrayList<ReviewItem>();



    public static void clearReviewItems(){
        REVIEW_ITEMS.clear();
    }

    public static void addReviewItem(ReviewItem item) {
        REVIEW_ITEMS.add(item); //used by recycler view
    }



    public static class ReviewItem {

        public final String id;
        public final String author;
        public final String content;
        public final String url;


        public ReviewItem(String id,
                         String author,
                         String content,
                         String url
        ) {
            this.id = id;
            this.author=author;
            this.content = content;
            this.url=url;

        }

        @Override
        public String toString() {
            return author;
        }
    }

}
