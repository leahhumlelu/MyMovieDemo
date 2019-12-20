package com.example.mymoviedemo.data_fetch;



import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.mymoviedemo.data_fetch.remote.MovieApiInterface;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    private static final String TAG = "MovieDataSource";
    private MovieListRepository movieListRepository;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private int sort;

    public MovieDataSource(MovieListRepository movieListRepository,int sort) {
        this.movieListRepository = movieListRepository;
        this.sort = sort;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        final int start_page =1;
        movieListRepository.getMovieList(sort,start_page,params.requestedLoadSize).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                callback.onResult(movies,null,start_page+1);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        movieListRepository.getMovieList(sort,params.key,params.requestedLoadSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        callback.onResult(movies,params.key+1);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
