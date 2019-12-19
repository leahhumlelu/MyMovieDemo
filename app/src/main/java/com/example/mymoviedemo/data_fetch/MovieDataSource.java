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
import io.reactivex.disposables.Disposable;

public class MovieDataSource extends PageKeyedDataSource<Pair<Integer,Integer>, Movie> {
    private static final String TAG = "MovieDataSource";
    private MovieListRepository movieListRepository;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;

    public MovieDataSource(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
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
    public void loadInitial(@NonNull final LoadInitialParams<Pair<Integer, Integer>> params, @NonNull final LoadInitialCallback<Pair<Integer, Integer>, Movie> callback) {
        final int sort = Util.SORT_BY_POPULAR;
        final int start_page =1;
        movieListRepository.getMovieList(sort,start_page,params.requestedLoadSize).subscribe(new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                callback.onResult(movies,null,Pair.create(sort,start_page+1));
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
    public void loadBefore(@NonNull LoadParams<Pair<Integer, Integer>> params, @NonNull LoadCallback<Pair<Integer, Integer>, Movie> callback) {
        //todo
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Pair<Integer, Integer>> params, @NonNull final LoadCallback<Pair<Integer, Integer>, Movie> callback) {
        movieListRepository.getMovieList(params.key.first,params.key.second,params.requestedLoadSize).subscribe(new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Movie> movies) {
                callback.onResult(movies,Pair.create(params.key.first,params.key.second+1));
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
