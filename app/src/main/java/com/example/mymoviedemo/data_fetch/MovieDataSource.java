package com.example.mymoviedemo.data_fetch;



import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.mymoviedemo.data_fetch.remote.MovieApiInterface;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.di.NoConnectivityException;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {
    private static final String TAG = "MovieDataSource";
    private MovieListRepository movieListRepository;
    private MutableLiveData<Status> networkState;
    private MutableLiveData<Status> initialLoading;
    private int sort;
    private CompositeDisposable compositeDisposable;
    private Completable retryCompletable;

    public MovieDataSource(MovieListRepository movieListRepository, int sort, CompositeDisposable compositeDisposable) {
        this.movieListRepository = movieListRepository;
        this.sort = sort;
        this.compositeDisposable = compositeDisposable;
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
    }

    public LiveData<Status> getNetworkState() {
        return networkState;
    }

    public LiveData<Status> getInitialLoading() {
        return initialLoading;
    }

    public void retry(){
        if(retryCompletable!=null){
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action() {
                        @Override
                        public void run() throws Exception {

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e(TAG, throwable.getMessage());
                        }
                    })
            );
        }
    }

    private void setRetry(final Action action){
        if(action==null){
            this.retryCompletable=null;
        }else{
            this.retryCompletable = Completable.fromAction(action);
        }
    }


    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        final int start_page =1;
        networkState.postValue(Status.LOADING);
        initialLoading.postValue(Status.LOADING);
        movieListRepository.getMovieList(sort,start_page,params.requestedLoadSize).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<Movie> movies) {
                setRetry(null);
                networkState.postValue(Status.SUCCESS);
                initialLoading.postValue(Status.SUCCESS);
                callback.onResult(movies,null,start_page+1);
            }

            @Override
            public void onError(Throwable e) {
                setRetry(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadInitial(params,callback);
                    }
                });
                if(e instanceof NoConnectivityException){
                    networkState.postValue(Status.NO_INTERNET);
                    initialLoading.postValue(Status.NO_INTERNET);
                }else{
                    networkState.postValue(Status.ERROR);
                    initialLoading.postValue(Status.ERROR);
                }
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
        networkState.postValue(Status.LOADING);
        movieListRepository.getMovieList(sort,params.key,params.requestedLoadSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Movie> movies) {
                        setRetry(null);
                        networkState.postValue(Status.SUCCESS);
                        callback.onResult(movies,params.key+1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRetry(new Action() {
                            @Override
                            public void run() throws Exception {
                                loadAfter(params, callback);
                            }
                        });
                        if(e instanceof NoConnectivityException){
                            networkState.postValue(Status.NO_INTERNET);
                        }else{
                            networkState.postValue(Status.ERROR);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
