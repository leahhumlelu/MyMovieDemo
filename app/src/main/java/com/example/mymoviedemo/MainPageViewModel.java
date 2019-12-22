package com.example.mymoviedemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;


import javax.inject.Inject;

import io.reactivex.Observable;


public class MainPageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;
    private LiveData<PagedList<Movie>> moviePagedList;

    @Inject
    public MainPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
    }

    public LiveData<PagedList<Movie>> getMovieList(int sort){
        MovieDataSourceFactory factory = new MovieDataSourceFactory(movieListRepository,sort);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(20)
                .build();
        moviePagedList = new LivePagedListBuilder<>(factory,pagedListConfig)
                .setInitialLoadKey(1)
                .build();
        return moviePagedList;
    }


}
