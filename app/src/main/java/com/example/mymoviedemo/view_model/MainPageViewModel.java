package com.example.mymoviedemo.view_model;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieDataSource;
import com.example.mymoviedemo.data_fetch.MovieDataSourceFactory;
import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.data_fetch.Status;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;


import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class MainPageViewModel extends ViewModel {
    private static final String TAG = "MainPageViewModel";
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
        networkState = new MutableLiveData<>();
        sortLiveData.postValue(Util.SORT_BY_POPULAR);
    }

    public void setSortLiveData(int sort) {
        sortLiveData.postValue(sort);
    }

    public MutableLiveData<Integer> getSortLiveData() {
        return sortLiveData;
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
        initialLoadingState = Transformations.switchMap(factory.getMovieDataSourceMutableLiveData(), new Function<MovieDataSource, LiveData<Status>>() {
            @Override
            public LiveData<Status> apply(MovieDataSource input) {
                return input.getInitialLoading();
            }
        });

        networkState = Transformations.switchMap(factory.getMovieDataSourceMutableLiveData(), new Function<MovieDataSource, LiveData<Status>>() {
            @Override
            public LiveData<Status> apply(MovieDataSource input) {
                return input.getNetworkState();
            }
        });
        return moviePagedList;
    }

    public LiveData<Status> getInitialLoadingState(){
        return initialLoadingState;
    }

    public LiveData<Status> getNetworkState() {
        return networkState;
    }

    public void retry(){
        factory.movieDataSource.retry();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
        Log.d(TAG, "onCleared: ");
    }
}
