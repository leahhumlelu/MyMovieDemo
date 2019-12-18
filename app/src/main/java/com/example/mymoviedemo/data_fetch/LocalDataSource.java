package com.example.mymoviedemo.data_fetch;

import androidx.lifecycle.MutableLiveData;

import com.example.mymoviedemo.data_fetch.local.MovieDao;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class LocalDataSource {
    private MovieDao movieDao;
    private Scheduler ioScheduler = Schedulers.io();

    @Inject
    public LocalDataSource(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Observable<List<Movie>> getMovieList(int sort) throws Exception {
        switch (sort){
            case Util.SORT_BY_TOP_RATED:
                return Observable.fromCallable(new Callable<List<Movie>>() {
                    @Override
                    public List<Movie> call() {
                        return movieDao.getTopRatedMovieList();
                    }
                }).subscribeOn(ioScheduler);
            default:
                return Observable.fromCallable(new Callable<List<Movie>>() {
                    @Override
                    public List<Movie> call(){
                        return movieDao.getPopularMovieList();
                    }
                }).subscribeOn(ioScheduler);
        }
    }

    public void saveMovieList(final List<Movie> movies){
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                movieDao.insertMovies(movies);
            }
        }).subscribeOn(ioScheduler);
    }

}
