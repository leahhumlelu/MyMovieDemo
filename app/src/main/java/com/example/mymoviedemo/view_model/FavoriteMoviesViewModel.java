package com.example.mymoviedemo.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieDataSourceFactory;
import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class FavoriteMoviesViewModel extends ViewModel {
    private MovieListRepository movieListRepository;
    private LiveData<PagedList<Movie>> moviePagedList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MovieDataSourceFactory factory;

    @Inject
    public FavoriteMoviesViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
        moviePagedList = getMovieList(Util.SORT_FAVORITE);
    }

    public LiveData<PagedList<Movie>> getMovieList(int sort){
        moviePagedList = new LivePagedListBuilder<>(movieListRepository.getFavoriteMovies(),50).build();
    /*    factory = new MovieDataSourceFactory(movieListRepository,sort,compositeDisposable);
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPageSize(20)
                .build();
        moviePagedList = new LivePagedListBuilder<>(factory,pagedListConfig)
                .setInitialLoadKey(1)
                .build();*/
        /*initialLoadingState = Transformations.switchMap(factory.getMovieDataSourceMutableLiveData(), new Function<MovieDataSource, LiveData<Status>>() {
            @Override
            public LiveData<Status> apply(MovieDataSource movieDataSource) {
                return movieDataSource.getInitialLoading();
            }
        });*/

        return moviePagedList;
    }

    public LiveData<PagedList<Movie>> getMoviePagedList() {
        return moviePagedList;
    }
}
