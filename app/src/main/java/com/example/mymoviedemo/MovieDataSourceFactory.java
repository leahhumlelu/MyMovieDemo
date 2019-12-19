package com.example.mymoviedemo;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.mymoviedemo.data_fetch.MovieDataSource;
import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;

import javax.inject.Inject;

import io.reactivex.Observable;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {
    public MovieDataSource movieDataSource;
    private MutableLiveData<MovieDataSource> movieDataSourceMutableLiveData;

    public MovieDataSourceFactory(MovieListRepository movieListRepository,int sort) {
        movieDataSource = new MovieDataSource(movieListRepository,sort);
        movieDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        movieDataSourceMutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
