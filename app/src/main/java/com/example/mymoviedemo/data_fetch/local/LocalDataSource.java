package com.example.mymoviedemo.data_fetch.local;

import android.telecom.Call;

import androidx.lifecycle.MutableLiveData;

import com.example.mymoviedemo.data_fetch.local.MovieDao;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieReviewResult;
import com.example.mymoviedemo.model.MovieTrailer;
import com.example.mymoviedemo.model.MovieTrailerResult;
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


    public Observable<List<Movie>> getMovieList(int sort, final int loadSize){
        switch (sort){
            case Util.SORT_BY_TOP_RATED:
                return Observable.fromCallable(new Callable<List<Movie>>() {
                    @Override
                    public List<Movie> call() {
                        return movieDao.getTopRatedMovieList(loadSize);
                    }
                }).subscribeOn(ioScheduler);
            case Util.SORT_FAVORITE:
                return Observable.fromCallable(new Callable<List<Movie>>() {
                    @Override
                    public List<Movie> call() throws Exception {
                        return movieDao.getFavoriteMovies();
                    }
                }).subscribeOn(ioScheduler);
            default:
                return Observable.fromCallable(new Callable<List<Movie>>() {
                    @Override
                    public List<Movie> call(){
                        return movieDao.getPopularMovieList(loadSize);
                    }
                }).subscribeOn(ioScheduler);
        }
    }


    public Observable<List<MovieTrailer>> getMovieTrailersById(final int movieId){
        return Observable.fromCallable(new Callable<List<MovieTrailer>>() {
            @Override
            public List<MovieTrailer> call() throws Exception {
                return movieDao.getMovieTrailersById(movieId);
            }
        }).subscribeOn(ioScheduler);
    }

    public Observable<List<MovieReview>> getMovieReviewsById(final int movieId){
        return Observable.fromCallable(new Callable<List<MovieReview>>() {
            @Override
            public List<MovieReview> call() throws Exception {
                return movieDao.getMovieReviewsById(movieId);
            }
        }).subscribeOn(ioScheduler);
    }

    public Completable saveMovieList(final List<Movie> movies){
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                movieDao.insertMovies(movies);
            }
        }).subscribeOn(ioScheduler);
    }


    public Completable saveMovieTrailers(final List<MovieTrailer> movieTrailers, final long movieId){
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                for(MovieTrailer movieTrailer:movieTrailers){
                    movieTrailer.setMovieId(movieId);
                }
                movieDao.insertMovieTrailers(movieTrailers);
            }
        });
    }

    public Completable saveMovieReviews(final List<MovieReview> movieReviews, final long movieId){
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                for(MovieReview movieReview:movieReviews){
                    movieReview.setMovieId(movieId);
                }
                movieDao.insertMovieReviews(movieReviews);
            }
        });
    }


    public Completable updateMovie(final Movie movie){
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                movieDao.updateMovie(movie);
            }
        }).subscribeOn(ioScheduler);
    }

    public Observable<Movie> getMovieById(final int movieId) {
        return Observable.fromCallable(new Callable<Movie>() {
            @Override
            public Movie call() throws Exception {
                return movieDao.getMovieById(movieId);
            }
        }).subscribeOn(ioScheduler);
    }

}
