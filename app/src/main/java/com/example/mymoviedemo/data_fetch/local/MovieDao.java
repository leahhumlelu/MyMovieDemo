package com.example.mymoviedemo.data_fetch.local;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieReviewResult;
import com.example.mymoviedemo.model.MovieTrailer;
import com.example.mymoviedemo.model.MovieTrailerResult;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC LIMIT :loadSize")
    List<Movie> getPopularMovieList(int loadSize);

    @Query("SELECT * FROM movie ORDER BY voteAverage DESC LIMIT :loadSize")
    List<Movie> getTopRatedMovieList(int loadSize);

    @Query("SELECT * FROM movietrailer WHERE movieId = :movieId")
    List<MovieTrailer> getMovieTrailersById(int movieId);

    @Query("SELECT * FROM moviereview WHERE movieId = :movieId")
    List<MovieReview> getMovieReviewsById(int movieId);

    @Query("SELECT * FROM movie WHERE favorite = 1")
    DataSource.Factory<Integer,Movie> getFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertMovies(List<Movie> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertMovieTrailers(List<MovieTrailer> movieTrailers);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertMovieReviews(List<MovieReview> movieReviews);

    @Query("SELECT * FROM Movie WHERE id= :movieId LIMIT 1")
    Movie getMovieById(int movieId);

    @Update
    public int updateMovie(Movie movie);
}
