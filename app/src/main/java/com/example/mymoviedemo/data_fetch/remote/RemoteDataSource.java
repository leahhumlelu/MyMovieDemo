package com.example.mymoviedemo.data_fetch.remote;

import com.example.mymoviedemo.data_fetch.local.LocalDataSource;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieListResult;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RemoteDataSource {
    private MovieApiInterface movieApiInterface;
    private Scheduler ioScheduler = Schedulers.io();
    private LocalDataSource localDataSource;

    @Inject
    public RemoteDataSource(MovieApiInterface movieApiInterface, LocalDataSource localDataSource) {
        this.movieApiInterface = movieApiInterface;
        this.localDataSource = localDataSource;
    }


    public Observable<List<Movie>> getMovieList(int sort, int page) throws Exception {
        Observable<MovieListResult> remoteData;
        switch (sort){
            case Util.SORT_BY_TOP_RATED:
                remoteData = movieApiInterface.getTopRatedMovieList(page);
                break;
            default:
                remoteData = movieApiInterface.getPopularMovieList(page);
                break;
        }
        return remoteData
                .map(new Function<MovieListResult, List<Movie>>() {
                    @Override
                    public List<Movie> apply(MovieListResult movieListResult) {
                        return movieListResult.getResults();
                    }
                })
                .doOnNext(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies){
                        //todo: data is not saved locally
                        localDataSource.saveMovieList(movies);
                    }
                })
                .subscribeOn(ioScheduler);

    }
}
