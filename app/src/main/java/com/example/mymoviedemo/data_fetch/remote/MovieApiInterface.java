package com.example.mymoviedemo.data_fetch.remote;

import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieListResult;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {

    @GET("movie/popular?")
    Observable<MovieListResult> getPopularMovieList(@Query("page") int page);

    @GET("movie/top_rated?")
    Observable<MovieListResult> getTopRatedMovieList(@Query("page") int page);

    @GET("movie/{movie_id}?")
    Observable<MovieDetailResult> getMovieDetailById(@Path("movie_id") int movieId);
}
