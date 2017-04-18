package com.example.spoorthy.moviesearch.com.example.spoorthy.moviesearch.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by spoorthy on 4/14/2017.
 */

public class MovieModel implements Serializable {
    private String movieName;
    private String releaseDate;
    private String moviePath;
    private String originalLanguage;
    private Bitmap movieImage;
    private String movieOverView;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate=releaseDate;
    }

    public String getMoviePath()
    {
        return moviePath;
    }

    public void setMoviePath(String moviePath)
    {
        this.moviePath=moviePath;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    public void setoriginalLanguage(String originalLanguage)
    {
        this.originalLanguage=originalLanguage;
    }

    public Bitmap getMovieImage()
    {
        return movieImage;
    }

    public void setMovieImage(Bitmap movieImage)
    {
        this.movieImage=movieImage;
    }

    public String getMovieOverView()
    {
        return movieOverView;
    }

    public void setMovieOverView(String movieOverView)
    {
        this.movieOverView=movieOverView;
    }

}
