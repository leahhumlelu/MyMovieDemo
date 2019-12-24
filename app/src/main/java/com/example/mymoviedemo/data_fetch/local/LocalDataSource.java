package com.example.mymoviedemo.data_fetch.local;

import android.telecom.Call;
import android.util.Log;

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
    private static final String TAG = "LocalDataSource";
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
        return Completable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                List<Long> rows = movieDao.insertMovies(movies);
                return rows;
            }
        }).subscribeOn(ioScheduler);
       /* return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                movieDao.insertMovies(movies);
            }
        }).subscribeOn(ioScheduler);*/
    }


    public Completable saveMovieTrailers(final List<MovieTrailer> movieTrailers, final long movieId){
        return Completable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                for(MovieTrailer movieTrailer:movieTrailers){
                    movieTrailer.setMovieId(movieId);
                }
                List<Long> rows = movieDao.insertMovieTrailers(movieTrailers);
                return rows;
            }
        }).subscribeOn(ioScheduler);
        /*return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                for(MovieTrailer movieTrailer:movieTrailers){
                    movieTrailer.setMovieId(movieId);
                }
                movieDao.insertMovieTrailers(movieTrailers);
            }
        });*/
    }

    public Completable saveMovieReviews(final List<MovieReview> movieReviews, final long movieId){
        return Completable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                for(MovieReview movieReview:movieReviews){
                    movieReview.setMovieId(movieId);
                }
                List<Long> rows = movieDao.insertMovieReviews(movieReviews);;
                return rows;
            }
        }).subscribeOn(ioScheduler);
        /*return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                for(MovieReview movieReview:movieReviews){
                    movieReview.setMovieId(movieId);
                }
                movieDao.insertMovieReviews(movieReviews);
            }
        });*/
    }


    public Observable<Integer> updateMovie(final Movie movie){
        return Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int row = movieDao.updateMovie(movie);
                return row;
            }
        }).subscribeOn(ioScheduler);
    }

    public Observable<Movie> getMovieById(final int movieId) {
        return Observable.fromCallable(new Callable<Movie>() {
            @Override
            public Movie call() throws Exception {
                Movie movie = movieDao.getMovieById(movieId);
                Log.d(TAG, "get movie by id: "+movieId+ "get: "+movie.getTitle()+" favorite: "+movie.getFavorite());
                return movie;
            }
        }).subscribeOn(ioScheduler);
    }

}
