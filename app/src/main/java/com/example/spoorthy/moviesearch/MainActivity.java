package com.example.spoorthy.moviesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.spoorthy.moviesearch.com.example.spoorthy.moviesearch.models.MovieModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText movieNameTxt;
    Button actionSearch,searchMovies;
    TextView searchText;
    static List<MovieModel> mMovies;
    static String API_KEY = "8d38aaebdd82b421b3589278905a53df";
    static String movieName = "";
    MovieModel[] result;
    List<MovieModel> persons;
    String posterPath;
    Bitmap movieBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add code for displaying the search button
        movieNameTxt = (EditText)findViewById(R.id.movie_txt);
        movieNameTxt.setVisibility(View.GONE);
        searchMovies = (Button)findViewById(R.id.search_movies);
        searchMovies.setVisibility(View.GONE);
        searchText = (TextView) findViewById(R.id.search_txt);
        searchText.setVisibility(View.GONE);
        actionSearch = (Button) findViewById(R.id.action_search);
        actionSearch.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                displaySearchView(view);
            }

        });
    }

    public void displaySearchView(View view){
        actionSearch.setVisibility(View.GONE);
        movieNameTxt.setVisibility(View.VISIBLE);
        searchMovies.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.VISIBLE);

        searchMovies.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

                if(movieNameTxt.getText().length() >0){
                    movieName = movieNameTxt.getText().toString();
                }
                new ImageLoadTask().execute();
            }
        });
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, List<MovieModel>> {

        @Override
        protected List<MovieModel> doInBackground(Void... params) {
            while(true){
                try{
                    mMovies = getPathsFromAPI(movieName);
                    return mMovies;
                }
                catch(Exception e)
                {
                    continue;
                }
            }

        }
        @Override
        protected void onPostExecute(List<MovieModel>result)
        {
            for(int i=0;i < result.size();i++){
                DisplaySearchResults(result);
            }
        }

        //displaying the search results
        public void DisplaySearchResults( List<MovieModel> result){
            Intent intent = new Intent(getApplicationContext(), SearchResultsActivity.class);
            intent.putExtra("movies", (Serializable) result);
            startActivity(intent);
        }

        public List<MovieModel> getPathsFromAPI(String movieName)
        {
            while(true)
            {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String JSONResult;

                try {
                    String urlString = null;
                    urlString = "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY+"&query="+ movieName;
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    JSONResult = buffer.toString();

                    try {
                        return getPathsFromJSON(JSONResult);
                    } catch (JSONException e) {
                        return null;
                    }
                }catch(Exception e)
                {
                    continue;
                }finally {
                    if(urlConnection!=null)
                    {
                        urlConnection.disconnect();
                    }
                    if(reader!=null)
                    {
                        try{
                            reader.close();
                        }catch(final IOException e)
                        {
                        }
                    }
                }


            }
        }


        //parsing the result returned from the server
        public List<MovieModel> getPathsFromJSON(String JSONStringParam) throws JSONException {
            MovieModel movieModel = new MovieModel();
            JSONObject JSONString = new JSONObject(JSONStringParam);
            persons = new ArrayList<>();
            JSONArray moviesArray = JSONString.getJSONArray("results");
            result = new MovieModel[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                String moviePath = movie.optString("poster_path");
                String movieTitle = movie.optString("title");
                String movieOverView = movie.optString("overview");

                movieModel = new MovieModel();

                if (movieTitle != null) {
                    movieModel.setMovieName(movieTitle);
                    String movieLanguage = movie.optString("original_language");
                    if(movieOverView != null){
                        if (movieLanguage != null) {
                            movieModel.setMovieOverView(movieOverView);
                            movieModel.setoriginalLanguage(movieLanguage);
                            String movieReleaseDate = movie.optString("release_date");
                            if (movieReleaseDate != null)
                                movieModel.setReleaseDate(movieReleaseDate);
                            if (moviePath != null) {
                                movieModel.setMoviePath(moviePath);
                                movieModel.setMovieImage(new ImageDownload().getImageBitmap());
                                result[i] = movieModel;
                                persons.add(movieModel);

                                System.out.println("movie name is " + result[i].getMovieName());
                            }
                        }
                    }
                }
            }
            System.out.println("result name is " + result.length);
            return persons;
        }
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

            setImageBitmap(bm);
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

        public  Bitmap getImageBitmap(){
            return movieBitmap;
        }
        public   void setImageBitmap(Bitmap b){
            movieBitmap = b;
        }
    }
}
