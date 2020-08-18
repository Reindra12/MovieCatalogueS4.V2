package com.reindra.submission4java.fragment.favorite;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reindra.submission4java.R;
import com.reindra.submission4java.activity.DetailActivity;
import com.reindra.submission4java.activity.TVDetailActivity;
import com.reindra.submission4java.adapter.TVAdapter;
import com.reindra.submission4java.database.MovieHelper;
import com.reindra.submission4java.database.TVHelper;
import com.reindra.submission4java.model.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTVFragment extends Fragment {
    private RecyclerView recyclerView;
    private TVAdapter tvAdapter;
    private ArrayList <Movie> list;
    private TVHelper tvHelper;
    private LinearLayout nodata;


    public FavTVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nodata = view.findViewById(R.id.view_no_data);
        recyclerView = view.findViewById(R.id.rv_tv);
        tvHelper = tvHelper.getInstance(getContext());
        list = new ArrayList<>();
        tvAdapter = new TVAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();
        tvHelper.open();
        list.clear();
        list.addAll(tvHelper.getdataTV());
        tvAdapter.setData(list);
        tvAdapter.notifyDataSetChanged();

        if (list.size() > 0){
            tvAdapter.setData(list);
            tvAdapter.notifyDataSetChanged();
        }else{
            nodata.setVisibility(View.VISIBLE);
        }

        Context context;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tvAdapter);

        tvAdapter.setOnItemClickCallBack(new TVAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClicked(Movie data) {
                showTV(data);
            }
        });
        tvHelper.close();

    }

    private void showTV(Movie movie) {
        Intent intent = new Intent(getContext(), TVDetailActivity.class);
        intent.putExtra(TVDetailActivity.FLAG_EXTATV, movie);
        startActivity(intent);

    }
}
