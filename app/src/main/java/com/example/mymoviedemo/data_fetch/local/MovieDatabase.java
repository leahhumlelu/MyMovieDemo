package com.example.mymoviedemo.data_fetch.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieListResult;

@Database(entities = {Movie.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
