package com.example.spoorthy.moviesearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spoorthy.moviesearch.com.example.spoorthy.moviesearch.models.MovieModel;

import org.xml.sax.InputSource;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by nkanchar on 4/14/2017.
 */
//MovieDetails class  is used for displaying the details of the class
public class MovieDetails extends AppCompatActivity {

    TextView movieName,movieLanguage,movieReleaseDate,movieOverView;
    ImageView movieImage;
    private MovieModel movies;
    String posterPath;
    public static final String imageUrl = "https://image.tmdb.org/t/p/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        //set the movie details to textviews
        movieName = (TextView)findViewById(R.id.name);
        movieImage = (ImageView)findViewById(R.id.movie_image);
        movieLanguage=(TextView)findViewById(R.id.movie_lang);
        movieReleaseDate=(TextView)findViewById(R.id.movieReleaseDate);
        movieOverView=(TextView)findViewById(R.id.movieoverViewText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movies = ((MovieModel) extras.getSerializable("Movies"));
        }

        movieName.setText(movies.getMovieName());
        movieReleaseDate.setText(movies.getReleaseDate());
        movieLanguage.setText(movies.getOriginalLanguage());
        movieOverView.setText(movies.getMovieOverView());
        posterPath = imageUrl + "w154" + movies.getMoviePath();
        movieImage.setImageURI(Uri.parse(posterPath));

        new DownloadMovieImage().execute();

    }

    //used for getting the image from the url
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // DownloadMovieImage class is used for downloading the movie image from internet
    class DownloadMovieImage extends AsyncTask<String, Void, Bitmap> {
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
            //set the imageview to bitmap
           movieImage.setImageBitmap(bm);
        }
    }

}


