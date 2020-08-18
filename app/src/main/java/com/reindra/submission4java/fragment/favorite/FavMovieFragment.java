package com.reindra.submission4java.fragment.favorite;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reindra.submission4java.R;
import com.reindra.submission4java.activity.DetailActivity;
import com.reindra.submission4java.adapter.MovieAdapter;
import com.reindra.submission4java.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.reindra.submission4java.database.DatabaseContract.MoviesColumns.CONTENT_MOVIE;
import static com.reindra.submission4java.database.MappingHelper.mapCursor;

interface LoadDataCallBack {
    void preExecute();

    void postExecute(Cursor cursor);
}

public class FavMovieFragment extends Fragment implements LoadDataCallBack {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private LinearLayout nodata;
    private static final String EXTRA_STATE = "extra_state";


    public FavMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movie, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieAdapter = new MovieAdapter(getContext());
        nodata = view.findViewById(R.id.view_no_data);
        recyclerView = view.findViewById(R.id.rv_category);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver movieObserver = new DataObserver(handler, getContext());

        if (getActivity() != null) {
            getActivity().getContentResolver().registerContentObserver(CONTENT_MOVIE, true, movieObserver);
        }
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickCallBack(new MovieAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(Movie data) {
                showMovie(data);

            }
        });
        if (savedInstanceState == null) {
            new LoadDataAsync(getContext(), this).execute();
        } else {
            ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (movies != null) {
                movieAdapter.setData(movies);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, movieAdapter.getFavorite());
    }

    private void showMovie(Movie movie) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        Uri uri = Uri.parse(CONTENT_MOVIE + "/" + movie.getId());
        intent.setData(uri);
        intent.putExtra(DetailActivity.FLAG_EXTRA, movie);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadDataAsync(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        ArrayList<Movie> movies = mapCursor(cursor);
        if (movies.size() > 0) {
            movieAdapter.setData(movies);
        } else {
            nodata.setVisibility(View.VISIBLE);
        }

    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }

    private static class LoadDataAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakReference;
        private final WeakReference<LoadDataCallBack> weakcallback;

        private LoadDataAsync(Context context, LoadDataCallBack callback) {
            weakReference = new WeakReference<>(context);
            weakcallback = new WeakReference<>(callback);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakcallback.get().preExecute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakcallback.get().postExecute(cursor);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakReference.get();
            return context.getContentResolver().query(CONTENT_MOVIE, null, null, null, null);

        }
    }

}

