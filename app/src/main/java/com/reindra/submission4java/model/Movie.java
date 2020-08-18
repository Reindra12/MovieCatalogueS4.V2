package com.reindra.submission4java.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.COUNTRY;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.OVERVIEW;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.POSTER;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.RATING;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.TITLE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.YEAR;
import static com.reindra.submission4java.database.DatabaseContract.getColumnInt;
import static com.reindra.submission4java.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;
    private String photo;
    private String title;
    private String date;
    private String rating;
    private String overview;
    private String country;

    protected Movie(Parcel in) {
        id = in.readInt();
        photo = in.readString();
        title = in.readString();
        date = in.readString();
        rating = in.readString();
        overview = in.readString();
        country = in.readString();
    }

    public Movie(){

    }

    public Movie(int id, String title, String photo, String overview, String country){
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.overview = overview;
        this.date = date;
        this.country = country;
    }


    public Movie(int id, String photo, String title, String rating, String date, String overview){
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.date = date;
        this.overview = overview;
        this.photo = photo;
//        this.country = country;
    }

    public Movie (Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.date = getColumnString(cursor, YEAR);
        this.photo = getColumnString(cursor, POSTER);
        this.rating = getColumnString(cursor, RATING);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.country = getColumnString(cursor, COUNTRY);
    }
   /* public Movie (int id, String title, String date, String rating, String country, String overview, String photo){
        this.id = id;
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.country = country;
        this.overview = overview;
        this.photo = photo;

    }*/

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(photo);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(rating);
        dest.writeString(overview);
        dest.writeString(country);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}