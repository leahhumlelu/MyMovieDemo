package com.example.mymoviedemo.data_fetch.local;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymoviedemo.model.Movie;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :loadSize")
    List<Movie> getPopularMovieList(int loadSize);

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC LIMIT :loadSize")
    List<Movie> getTopRatedMovieList(int loadSize);

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :loadSize")
    DataSource.Factory<Integer,Movie> getPopularMovieDataSource(int loadSize);

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC LIMIT :loadSize")
    DataSource.Factory<Integer,Movie> getTopRatedMovieDataSource(int loadSize);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

}
