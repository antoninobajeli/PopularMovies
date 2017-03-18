package com.example.antoninobajeli.popularmovies.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoninobajeli on 18/03/17.
 */

public class VideosContent {
    private static final String LOG_TAG=VideosContent.class.getSimpleName();

    public static final List<VideoItem> VIDEO_ITEMS = new ArrayList<VideoItem>();



    public static void clearVideoItems(){
        VIDEO_ITEMS.clear();
    }
    public static void addVideoItem(VideoItem item) {
        VIDEO_ITEMS.add(item); //used by recycler view
        //ITEM_MAP.put(item.id, item);
    }


    public static class VideoItem {

        public final String videoId;
        public final String language;
        public final String country;
        public final String key;
        public final String name;
        public final String site;
        public final String size;
        public final String type;


        public VideoItem(String videoId,
                         String language,
                         String country,
                         String key,
                         String name,
                         String site,
                         String size,
                         String type
        ) {
            this.videoId = videoId;
            this.language=language;
            this.country = country;
            this.key=key;
            this.name = name;
            this.site=site;
            this.size= size;
            this.type = type;

        }

        @Override
        public String toString() {
            return name;
        }
    }
}
