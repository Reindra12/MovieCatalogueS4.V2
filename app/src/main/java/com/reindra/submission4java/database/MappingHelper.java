package com.reindra.submission4java.database;

import android.database.Cursor;

import com.reindra.submission4java.model.Movie;

import java.util.ArrayList;

import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.COUNTRY;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.OVERVIEW;
//import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.PHOTO;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.POSTER;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.RATING;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.TITLE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.YEAR;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns._ID;

public class MappingHelper {
    public static ArrayList<Movie> mapCursor(Cursor itemCursor) {
        ArrayList<Movie> items = new ArrayList<>();

        while (itemCursor.moveToNext()) {
            int id = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(_ID));
            String photo = itemCursor.getString(itemCursor.getColumnIndexOrThrow(POSTER));
            String tittle = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TITLE));
           String rating = itemCursor.getString(itemCursor.getColumnIndexOrThrow(RATING));
            String overview = itemCursor.getString(itemCursor.getColumnIndexOrThrow(OVERVIEW));
            String year = itemCursor.getString(itemCursor.getColumnIndexOrThrow(YEAR));
            String country = itemCursor.getString(itemCursor.getColumnIndexOrThrow(COUNTRY));

            items.add(new Movie(id, photo, tittle, rating, year, overview));
//            items.add(new Movie(id, photo, tittle));
        }
        return items;

    }

    public static ArrayList<Movie> mapCursorTV(Cursor itemCursor) {
        ArrayList<Movie> item = new ArrayList<>();
        while (itemCursor.moveToNext()) {
            int id = itemCursor.getInt(itemCursor.getColumnIndexOrThrow(_ID));
            String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(TITLE));
            String photo = itemCursor.getString(itemCursor.getColumnIndexOrThrow(POSTER));
            String overview = itemCursor.getString(itemCursor.getColumnIndexOrThrow(OVERVIEW));
            String date = itemCursor.getString(itemCursor.getColumnIndexOrThrow(YEAR));
            String country = itemCursor.getString(itemCursor.getColumnIndexOrThrow(COUNTRY));

          item.add(new Movie(id, title, photo, overview, country));
        }
        return item;
    }
}