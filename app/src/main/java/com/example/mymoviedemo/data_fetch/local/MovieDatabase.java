package com.example.mymoviedemo.data_fetch.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieListResult;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieReviewResult;
import com.example.mymoviedemo.model.MovieTrailer;
import com.example.mymoviedemo.model.MovieTrailerResult;

@Database(entities = {Movie.class, MovieTrailer.class, MovieReview.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
