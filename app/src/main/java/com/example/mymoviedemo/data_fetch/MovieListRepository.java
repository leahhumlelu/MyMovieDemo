package com.example.mymoviedemo.data_fetch;

import android.util.Log;

import com.example.mymoviedemo.data_fetch.local.LocalDataSource;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class MovieListRepository {
    private static final String TAG = "MovieListRepository";
    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    @Inject
    public MovieListRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Observable<List<Movie>> getMovieList(int sort, int page, int loadSize){
        try{
            Observable<List<Movie>> remoteData = remoteDataSource.getMovieList(sort, page);
            Observable<List<Movie>> localData = localDataSource.getMovieList(sort,loadSize);
            return Observable.zip(remoteData, localData, new BiFunction<List<Movie>, List<Movie>, List<Movie>>() {
                @Override
                public List<Movie> apply(List<Movie> movies, List<Movie> movies2) {
                    return movies!=null? movies:movies2;
                }
            });
        }catch (Exception e){
            Log.d(TAG, "error in fetching movie list",e );
            return null;
        }

    }
}
