package com.reindra.submission4java.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.reindra.submission4java.R;
import com.reindra.submission4java.database.MovieHelper;
import com.reindra.submission4java.model.Movie;

import static android.provider.BaseColumns._ID;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.CONTENT_MOVIE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.COUNTRY;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.OVERVIEW;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.POSTER;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.RATING;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.TITLE;
import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.YEAR;

public class DetailActivity extends AppCompatActivity {
    public static final String FLAG_EXTRA = "flag_extra";
    ProgressBar progressBar;
    Movie movie = new Movie();
    private MovieHelper movieHelper;
    TextView title, overview, date, rate, notif, toolbarText;
    ImageView poster, favorite;
    Uri uri;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarText = findViewById(R.id.toolbar_text);
        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.tv_tittle_detail);
        overview = findViewById(R.id.tv_synopsis);
        date = findViewById(R.id.tv_date_movie);
        rate = findViewById(R.id.tv_score_detail);
        poster = findViewById(R.id.iv_poster_detail);
        TextView tvCountry = findViewById(R.id.tv_country_detail);
        RatingBar ratingbar = findViewById(R.id.rb_score);
        favorite = findViewById(R.id.iv_heartdetail);

        movie = getIntent().getParcelableExtra(FLAG_EXTRA);

        movieHelper = MovieHelper.getInstance(this);
//        movieHelper.open();

        showloading(true);
        if (movie != null) {
            toolbarText.setText(movie.getTitle());
            title.setText(movie.getTitle());
            date.setText(movie.getDate());
            Float count = Float.parseFloat(movie.getRating());
            count = Float.valueOf(count * 10);
            rate.setText(String.format("%s%%", count.intValue()));
            ratingbar.setRating(count / 20f);
            overview.setText(movie.getOverview());
            tvCountry.setText(movie.getCountry());
            Glide.with(this)
                    .load(movie.getPhoto())
                    .into(poster);
            showloading(false);


            movieHelper.open();
            if (movieHelper.getAll(movie.getId())) {
                favorite.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
//                addItemToFavorite();
            }
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movieHelper.getAll(movie.getId())) {
                    dialog();
                } else {
                    favorite.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    deleteItem();
                }
            }
        });
    }

    private void showloading(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void dialog() {
        ViewGroup viewGroup = findViewById(R.id.content);
        View DialogviView = LayoutInflater.from(this).inflate(R.layout.dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(DialogviView);
        final AlertDialog alertDialog = builder.create();
        Button btnalert = DialogviView.findViewById(R.id.btnyes);
        ImageView clear = DialogviView.findViewById(R.id.imgclear);
        notif = DialogviView.findViewById(R.id.tvnotif);

        notif.setText(R.string.notifadd);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        btnalert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieHelper.open();
                favorite.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                addItemToFavorite();
                alertDialog.dismiss();
            }
        });
        movieHelper.close();
        alertDialog.show();
    }

    private void deleteItem() {
        uri = Uri.parse(CONTENT_MOVIE + "/" + movie.getId());
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
      /*  if (uri != null) {
            Toast.makeText(this, getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
        } else {
        }*/
    }

    private void addItemToFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, movie.getId());
        contentValues.put(TITLE, movie.getTitle());
        contentValues.put(YEAR, movie.getDate());
        contentValues.put(OVERVIEW, movie.getOverview());
        contentValues.put(POSTER, movie.getPhoto());
        contentValues.put(RATING, movie.getRating());
        contentValues.put(COUNTRY, movie.getCountry());

        getContentResolver().insert(CONTENT_MOVIE, contentValues);
        if (contentValues != null) {
            Toast.makeText(this, getResources().getString(R.string.add), Toast.LENGTH_SHORT).show();
            favorite.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            favorite.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.godown);
    }

}

