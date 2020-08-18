package com.reindra.submission4java.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_MOVIES = "Movies";
    public static final String TABLE_TV = "TVShow";
    public static String AUTHORITY = "com.reindra.submission4java";
    private static final String SCEME = "content";


    public static String getColumnString(Cursor cursor, String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    public static int getColumnInt(Cursor cursor, String column) {
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    public static final class MoviesColumns implements BaseColumns {
        public static final Uri CONTENT_MOVIE = new Uri.Builder().scheme(SCEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIES)
                .build();
        public static final Uri CONTENT_TV = new Uri.Builder().scheme(SCEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV)
                .build();
        public static String TITLE = "title";
        public static String YEAR = "date";
        public static String POSTER = "photo";
        public static String RATING = "rating";
        public static String OVERVIEW = "overview";
        public static String COUNTRY = "country";

    }
}

   /* public static final class TVColumns implements BaseColumns {
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String PHOTO = "photo";
        public static String RATING = "rating";
        public static String DATE = "date";

    }*/
