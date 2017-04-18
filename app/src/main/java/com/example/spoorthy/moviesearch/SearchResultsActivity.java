package com.example.spoorthy.moviesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.spoorthy.moviesearch.com.example.spoorthy.moviesearch.models.MovieModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by spoorthy on 4/14/2017.
 */

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MovieModel> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMovies = (List<MovieModel>) getIntent().getExtras().getSerializable("movies");
            if(mMovies.size() > 0){
                populateView(mMovies);
            }
        }
    }

    //populate the view with search results
    public void populateView(List<MovieModel> models){
        mRecyclerView = (RecyclerView) findViewById(R.id.search_movie_results_recycler_view);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SearchResultAdapter resultsAdapter = new SearchResultAdapter(mMovies);
        resultsAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void OnMovieResultsItemClick(View view, int position) {
                Intent intent = new Intent(SearchResultsActivity.this, MovieDetails.class);
                intent.putExtra("Movies", mMovies.get(position));
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(resultsAdapter);
    }


}
