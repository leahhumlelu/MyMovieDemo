package com.example.mymoviedemo.data_fetch;

import android.util.Log;

import com.example.mymoviedemo.data_fetch.local.LocalDataSource;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MovieListRepository {
    private static final String TAG = "MovieListRepository";
    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;
    private Scheduler ioScheduler = Schedulers.io();

    @Inject
    public MovieListRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Observable<List<Movie>> getMovieList(final int sort, int page, final int loadSize){
        Observable<List<Movie>> remoteData = remoteDataSource.getMovieList(sort, page)
                .doOnNext(new Consumer<List<Movie>>() {
                    @Override
                    public void accept(List<Movie> movies){
                        //todo: data is not saved locally
                        localDataSource.saveMovieList(movies);
                    }
                }).subscribeOn(ioScheduler);

        Observable<List<Movie>> localData = localDataSource.getMovieList(sort, loadSize);
        return Observable.zip(remoteData, localData, new BiFunction<List<Movie>, List<Movie>, List<Movie>>() {
            @Override
            public List<Movie> apply(List<Movie> movies, List<Movie> movies2) {
                //todo this function seems not working
                if(movies==null||movies.isEmpty()){
                    return movies2;
                }else{
                    return movies;
                }
            }
        });
            /*return remoteData.publish(new Function<Observable<List<Movie>>, ObservableSource<List<Movie>>>() {
                @Override
                public ObservableSource<List<Movie>> apply(Observable<List<Movie>> listObservable) throws Exception {
                    return Observable.mergeDelayError(listObservable,localDataSource.getMovieList(sort, loadSize).takeUntil(listObservable));
                }
            }).subscribeOn(Schedulers.io());*/
    }

    public Observable<MovieDetailResult> getMovieById(int movieId){
        try{
            Observable<MovieDetailResult> remoteData = remoteDataSource.getMovieById(movieId)
                    .doOnNext(new Consumer<MovieDetailResult>() {
                        @Override
                        public void accept(MovieDetailResult movieDetailResult) throws Exception {
                            localDataSource.saveMovieDetail(movieDetailResult);
                        }
                    }).subscribeOn(ioScheduler);
            Observable<MovieDetailResult> localData = localDataSource.getMovieDetailById(movieId);

            return Observable.zip(remoteData, localData, new BiFunction<MovieDetailResult, MovieDetailResult, MovieDetailResult>() {
                @Override
                public MovieDetailResult apply(MovieDetailResult movieDetailResult, MovieDetailResult movieDetailResult2) throws Exception {
                    return movieDetailResult==null? movieDetailResult:movieDetailResult2;
                }
            });
        }catch (Exception e){
            Log.d(TAG, "getMovieById: ");
            return null;
        }
    }
}
