package com.example.mymoviedemo.data_fetch;

import android.util.Log;

import androidx.paging.DataSource;

import com.example.mymoviedemo.data_fetch.local.LocalDataSource;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieReviewResult;
import com.example.mymoviedemo.model.MovieTrailer;
import com.example.mymoviedemo.model.MovieTrailerResult;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieListRepository {
    private static final String TAG = "MovieListRepository";
    private RemoteDataSource remoteDataSource;
    private LocalDataSource localDataSource;
    private Scheduler ioScheduler = Schedulers.io();
    private Scheduler mainScheduler = AndroidSchedulers.mainThread();

    @Inject
    public MovieListRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public DataSource.Factory<Integer,Movie> getFavoriteMovies(){
        return localDataSource.getFavoriteMovie();
    }


    public Observable<List<Movie>> getMovieList(final int sort, int page, final int loadSize){
        switch (sort){
            case Util
                    .SORT_FAVORITE:
                return localDataSource.getMovieList(sort, loadSize);
            default:
                Observable<List<Movie>> remoteData = remoteDataSource.getMovieList(sort, page)
                        .doOnNext(new Consumer<List<Movie>>() {
                            @Override
                            public void accept(List<Movie> movies){
                                //todo: data is not saved locally
                                localDataSource.saveMovieList(movies).subscribe(new DisposableCompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "onComplete: movie list has been saved to db successfully");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "onComplete: movie list are not saed to db. error" +e.getMessage());
                                    }
                                });
                            }
                        }).
                                subscribeOn(ioScheduler);

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
        }

            /*return remoteData.publish(new Function<Observable<List<Movie>>, ObservableSource<List<Movie>>>() {
                @Override
                public ObservableSource<List<Movie>> apply(Observable<List<Movie>> listObservable) throws Exception {
                    return Observable.mergeDelayError(listObservable,localDataSource.getMovieList(sort, loadSize).takeUntil(listObservable));
                }
            }).subscribeOn(Schedulers.io());*/
    }

    public Observable<List<MovieTrailer>> getMovieTrailers(final int movieId){
        Observable<List<MovieTrailer>> remoteData = remoteDataSource.getMovieTrailersById(movieId)
                .doOnNext(new Consumer<List<MovieTrailer>>() {
                    @Override
                    public void accept(List<MovieTrailer> movieTrailers) {
                        localDataSource.saveMovieTrailers(movieTrailers,movieId).subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: movie trailers have been saved to db successfully");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onComplete: movie trailers are not saed to db. error" +e.getMessage());
                            }
                        });
                    }
                }).subscribeOn(ioScheduler);

        Observable<List<MovieTrailer>> localData = localDataSource.getMovieTrailersById(movieId);
        return Observable.zip(remoteData, localData, new BiFunction<List<MovieTrailer>, List<MovieTrailer>, List<MovieTrailer>>() {
            @Override
            public List<MovieTrailer> apply(List<MovieTrailer> movieTrailers, List<MovieTrailer> movieTrailers2) throws Exception {
                if(movieTrailers==null || movieTrailers.isEmpty()){
                    return movieTrailers2;
                }
                return movieTrailers;
            }
        }).observeOn(mainScheduler);
    }

    public Observable<List<MovieReview>> getMovieReviews(final int movieId, int page){
        Observable<List<MovieReview>> remoteData = remoteDataSource.getMovieReviewsById(movieId,page)
                .doOnNext(new Consumer<List<MovieReview>>() {
                    @Override
                    public void accept(List<MovieReview> movieReviews) throws Exception {
                        localDataSource.saveMovieReviews(movieReviews,movieId).subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: movie reviews have been saved to db successfully");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onComplete: movie reviews are not saed to db. error" +e.getMessage());
                            }
                        });
                    }
                }).subscribeOn(ioScheduler);

        Observable<List<MovieReview>> localData = localDataSource.getMovieReviewsById(movieId);
        return Observable.zip(remoteData, localData, new BiFunction<List<MovieReview>, List<MovieReview>, List<MovieReview>>() {
            @Override
            public List<MovieReview> apply(List<MovieReview> movieReviews, List<MovieReview> movieReviews2) {
                if(movieReviews==null || movieReviews.isEmpty()){
                    return movieReviews2;
                }
                return movieReviews;
            }
        });
    }

    public Observable<Integer> updateMovie(Movie movie){
        return localDataSource.updateMovie(movie);
    }

    public Observable<Movie> getLocalMovieById(int movieId) {
        return localDataSource.getMovieById(movieId).subscribeOn(mainScheduler);
    }

    public Observable<Movie> getRemoteMovieById(int movieId) {
        return remoteDataSource.getMovieById(movieId).doOnNext(new Consumer<Movie>() {
            @Override
            public void accept(Movie movie) throws Exception {
                localDataSource.updateMovie(movie);
            }
        });
    }
}
