package com.reindra.submission4java.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reindra.submission4java.model.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static android.provider.ContactsContract.Contacts.Photo.PHOTO;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.OVERVIEW;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.RATING;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.TITLE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.YEAR;

import static com.reindra.submission4java.database.DatabaseContract.TABLE_TV;

public class TVHelper {
    private static final String DB_TABLE = TABLE_TV;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;
    private static TVHelper INSTANCE;

    private TVHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TVHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TVHelper(context);


                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Movie> getdataTV() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DB_TABLE, null, null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;

        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(PHOTO)));
                movie.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));
                movie.setDate(cursor.getString(cursor.getColumnIndexOrThrow(YEAR)));
                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(PHOTO, movie.getPhoto());
        args.put(RATING, movie.getRating());
        args.put(YEAR, movie.getDate());
        return database.insert(DB_TABLE, null, args);
    }

    public int delete(int id) {
        return database.delete(DB_TABLE, _ID + " = '" + id + "'", null);
    }

    public boolean getAllTV(int id) {
        String query = "SELECT * FROM " + DB_TABLE + " WHERE " + _ID + " =?";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        boolean exist = false;
        if (cursor.moveToFirst()) {
            exist = true;
        }
        cursor.close();
        return exist;
    }
    public Cursor queryById(String id) {
        return database.query(DB_TABLE, null, _ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor query() {
        return database.query(DB_TABLE, null, null, null, null, null, _ID + " ASC", null);
    }
    public long insertProv(ContentValues values) {
        return database.insert(DB_TABLE, null, values);
    }

    public int deleteProv(String id) {
        return database.delete(DB_TABLE, _ID + " = ?", new String[]{id});
    }
}

