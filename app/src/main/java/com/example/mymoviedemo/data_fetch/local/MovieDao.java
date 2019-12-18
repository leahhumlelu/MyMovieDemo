package com.example.mymoviedemo.data_fetch.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymoviedemo.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT 500")
    List<Movie> getPopularMovieList();

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC LIMIT 500")
    List<Movie> getTopRatedMovieList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMovies(List<Movie> movies);

}
