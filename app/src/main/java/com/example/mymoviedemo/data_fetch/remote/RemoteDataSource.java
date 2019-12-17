package com.example.mymoviedemo.data_fetch.remote;

import com.example.mymoviedemo.model.Movie;

import java.util.List;

import retrofit2.Call;

public class RemoteDataSource {
    private MovieApiInterface movieApiInterface;

    public RemoteDataSource(MovieApiInterface movieApiInterface) {
        this.movieApiInterface = movieApiInterface;
    }

    public Call<List<Movie>> getMovieList(){
        return movieApiInterface.getMovieList();
    }
}
