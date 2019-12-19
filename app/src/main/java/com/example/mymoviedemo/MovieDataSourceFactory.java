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

public class MovieDataSourceFactory extends DataSource.Factory<Pair<Integer,Integer>, Movie> {
    public MovieDataSource movieDataSource;
    private MutableLiveData<MovieDataSource> movieDataSourceMutableLiveData;

    public MovieDataSourceFactory(MovieListRepository movieListRepository) {
        movieDataSource = new MovieDataSource(movieListRepository);
        movieDataSourceMutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Pair<Integer, Integer>, Movie> create() {
        movieDataSourceMutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
