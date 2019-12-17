package com.example.mymoviedemo.data_fetch.remote;

import com.example.mymoviedemo.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApiInterface {
    //TODO:
    @GET()
    Call<List<Movie>> getMovieList();
}
