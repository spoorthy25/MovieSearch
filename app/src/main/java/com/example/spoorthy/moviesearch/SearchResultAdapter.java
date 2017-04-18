package com.example.spoorthy.moviesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.spoorthy.moviesearch.com.example.spoorthy.moviesearch.models.MovieModel;

/**
 * Created by spoorthy on 4/14/2017.
 */
//SearchResultAdapter is used for displaying the search results and assign the click event to each of the movie
public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ResultMovieViewHolder.OnClickListener {

    private List<MovieModel> searchedMovies;
    private MovieModel movie;
    private OnItemClickListener mOnItemClickListener;
    String posterPath;
    public static final String imageUrl = "https://image.tmdb.org/t/p/";
    ResultMovieViewHolder movieHolder;
    /**
     * Interface for Search Results OnClick event.
     */
    public interface OnItemClickListener {
        void OnMovieResultsItemClick(View view, int position);
    }

    /**
     * @param mItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

  /*  /**
     * Contructor: Takes in a list of Movies.
     * @param movies
     */
    public SearchResultAdapter(List<MovieModel> movies){
        this.searchedMovies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View resultsView;
        switch (viewType) {
            case 0:
                resultsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_movies_result, parent, false);
                ResultMovieViewHolder viewHolder = new ResultMovieViewHolder(resultsView, this);
                return viewHolder;
            default:
                resultsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_movies_result, parent, false);
                return new ResultMovieViewHolder(resultsView, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (position < searchedMovies.size()) {

            movie = searchedMovies.get(position);
            posterPath = imageUrl + "w154" + movie.getMoviePath();
                    movieHolder = (ResultMovieViewHolder) holder;
            new ImageDownload().execute();

            movieHolder.name.setText(movie.getMovieName());
                    movieHolder.dateOfRelease.setText(movie.getReleaseDate());
                    if (movie.getOriginalLanguage() != null)
                        movieHolder.language.setText(movie.getOriginalLanguage());

                    movieHolder.imageView.setImageBitmap(movie.getMovieImage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < searchedMovies.size())
            return 0;
        return  0;
    }


    @Override
    public int getItemCount() {
        return searchedMovies.size();
    }

    @Override
    public void OnPersonResultsItemClick(View view, int position) {
        this.mOnItemClickListener.OnMovieResultsItemClick(view, position);
    }

    class ImageDownload extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;

        protected Bitmap doInBackground(String... urls) {
            try {
                return getBitmapFromURL(posterPath);
            } catch (Exception e) {
                this.exception = e;

                return null;
            }
        }

        protected void onPostExecute(Bitmap bm) {
            movieHolder.imageView.setImageBitmap(bm);
        }

        public  Bitmap getBitmapFromURL(String src) {
            try {
                Log.e("src",src);
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Log.e("Bitmap","returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
                return null;
            }
        }
    }
}

