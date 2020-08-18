package com.reindra.submission4java.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

//    private static String DATABASE_NAME = "dbmoviecatalogue";
    private static final int DATABASE_VERSION = 1;


    private static final String SQL_CREATE_TABLE_MOVIES = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_MOVIES,
            DatabaseContract.MoviesColumns._ID,
            DatabaseContract.MoviesColumns.POSTER,
            DatabaseContract.MoviesColumns.TITLE,
            DatabaseContract.MoviesColumns.YEAR,
            DatabaseContract.MoviesColumns.RATING,
            DatabaseContract.MoviesColumns.OVERVIEW,
            DatabaseContract.MoviesColumns.COUNTRY
    );

    private static final String SQL_CREATE_TABLE_TV_SHOW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    /*" %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +*/
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_TV,
            DatabaseContract.MoviesColumns._ID,
            DatabaseContract.MoviesColumns.TITLE,
            DatabaseContract.MoviesColumns.OVERVIEW,
            DatabaseContract.MoviesColumns.POSTER
           /* DatabaseContract.TVColomns.RATING,
            DatabaseContract.TVColomns.DATE*/
    );

    public static String DATABASE_NAME = "dbmoviecatalogue";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TV);
        onCreate(db);
    }
}
