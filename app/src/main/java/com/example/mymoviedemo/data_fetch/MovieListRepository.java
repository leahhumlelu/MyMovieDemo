package com.example.mymoviedemo.data_fetch;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieListResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListRepository {
    private static final String TAG = "MovieListRepository";
    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;

    @Inject
    public MovieListRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public Observable<List<Movie>> getMovieList(int sort, int page){
        try{
            Observable<List<Movie>> remoteData = remoteDataSource.getMovieList(sort, page);
            Observable<List<Movie>> localData = localDataSource.getMovieList(sort);
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
