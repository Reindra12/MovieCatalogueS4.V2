package com.reindra.submission4java.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reindra.submission4java.database.DatabaseContract;
import com.reindra.submission4java.database.MovieHelper;
import com.reindra.submission4java.database.TVHelper;
import com.reindra.submission4java.fragment.favorite.FavMovieFragment;
import com.reindra.submission4java.fragment.favorite.FavTVFragment;

import static com.reindra.submission4java.database.DatabaseContract.AUTHORITY;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.CONTENT_MOVIE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.CONTENT_TV;
//import static com.reindra.submission4java.database.DatabaseContract.TVColomns.CONTENT_TV;
import static com.reindra.submission4java.database.DatabaseContract.TABLE_MOVIES;
import static com.reindra.submission4java.database.DatabaseContract.TABLE_TV;

public class FavProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV_SHOW = 3;
    private static final int TV_SHOW_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private MovieHelper movieHelper;
    private TVHelper tvHelper;
    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIES, MOVIE);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIES + "/#",
                MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV_SHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV + "/#", TV_SHOW_ID);
    }



    public FavProvider() {

    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        tvHelper = TVHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        movieHelper.open();
        tvHelper.open();
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.query();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                break;
            case TV_SHOW:
                cursor = tvHelper.query();
                break;
            case TV_SHOW_ID:
                cursor = tvHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert( Uri uri, ContentValues contentValues) {
        Uri uri1;
        long added;
        tvHelper.open();
        movieHelper.open();
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProv(contentValues);
                uri1 = Uri.parse(CONTENT_MOVIE + "/" + added);
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE, new FavMovieFragment.DataObserver(new Handler(), getContext()));
                break;
            case TV_SHOW:
                added = tvHelper.insertProv(contentValues);
                uri1 = Uri.parse(CONTENT_TV + "/" + added);
                if (getContext() != null){
                    getContext().getContentResolver().notifyChange(CONTENT_TV, null);
                }
                break;
            default:
                throw new SQLException("Failed" + uri);
        }
        return uri1;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        movieHelper.open();
        tvHelper.open();
        int drop;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                drop = movieHelper.deleteProv(uri.getLastPathSegment());
                if (getContext() != null){
                    getContext().getContentResolver().notifyChange(CONTENT_MOVIE, new FavMovieFragment.DataObserver(new Handler(),getContext()));
                }

                break;
            case TV_SHOW_ID:
                drop = tvHelper.deleteProv(uri.getLastPathSegment());
               if (getContext() !=null){
                   getContext().getContentResolver().notifyChange(CONTENT_TV, null);
               }
                break;
            default:
                drop = 0;
                break;
        }
        return drop;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
