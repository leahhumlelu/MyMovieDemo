package com.example.mymoviedemo;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;


import javax.inject.Inject;


public class MainPageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;
    private int page = 1;
    private LiveData<PagedList<Movie>> moviePagedList;

    @Inject
    public MainPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
    }

    public LiveData<PagedList<Movie>> getMovieList(int sort){
        MovieDataSourceFactory factory = new MovieDataSourceFactory(movieListRepository);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setPageSize(20)
                .build();
        moviePagedList = new LivePagedListBuilder<>(factory,pagedListConfig)
                .setInitialLoadKey(Pair.create(sort,1))
                .build();
        return moviePagedList;
    }
}
