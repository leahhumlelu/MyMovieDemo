package com.example.mymoviedemo;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieDataSource;
import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.data_fetch.Status;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.utilities.Util;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;


public class MainPageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;
    private LiveData<PagedList<Movie>> moviePagedList;
    private MutableLiveData<Integer> sortLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MovieDataSourceFactory factory;
    private LiveData<Status> initialLoadingState;
    private LiveData<Status> networkState;

    @Inject
    public MainPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
        sortLiveData = new MutableLiveData<>();
        initialLoadingState = new MutableLiveData<>();
        sortLiveData.postValue(Util.SORT_BY_POPULAR);
        moviePagedList = Transformations.switchMap(sortLiveData, new Function<Integer, LiveData<PagedList<Movie>>>() {
            @Override
            public LiveData<PagedList<Movie>> apply(Integer input) {
                return getMovieList(input);
            }
        });

    }

    public void setSortLiveData(int sort) {
        sortLiveData.postValue(sort);
    }

    public LiveData<PagedList<Movie>> getMoviePagedList() {
        return moviePagedList;
    }

    public LiveData<PagedList<Movie>> getMovieList(int sort){
        factory = new MovieDataSourceFactory(movieListRepository,sort,compositeDisposable);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(20)
                .build();
        moviePagedList = new LivePagedListBuilder<>(factory,pagedListConfig)
                .setInitialLoadKey(1)
                .build();
        /*initialLoadingState = Transformations.switchMap(factory.getMovieDataSourceMutableLiveData(), new Function<MovieDataSource, LiveData<Status>>() {
            @Override
            public LiveData<Status> apply(MovieDataSource movieDataSource) {
                return movieDataSource.getInitialLoading();
            }
        });*/

        return moviePagedList;
    }

    public LiveData<Status> getInitialLoadingState(){
        return initialLoadingState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
