package com.example.mymoviedemo.view_model;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieTrailer;
import com.example.mymoviedemo.model.MovieTrailerResult;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DetailPageViewModel extends ViewModel {
    private static final String TAG = "DetailPageViewModel";
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Movie> movieLiveData;
    private MutableLiveData<Integer> favoriteLiveData;
    private MutableLiveData<List<MovieTrailer>> trailerLiveData;
    private MutableLiveData<List<MovieReview>> reviewsLiveData;
    private MutableLiveData<Integer> movieIdLiveData;

    @Inject
    public DetailPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
        movieLiveData = new MutableLiveData<>();
        favoriteLiveData = new MutableLiveData<>();
        trailerLiveData = new MutableLiveData<>();
        reviewsLiveData = new MutableLiveData<>();
        movieIdLiveData = new MutableLiveData<>();
       /* Transformations.switchMap(movieIdLiveData, new Function<Integer, LiveData<Movie>>() {
            @Override
            public LiveData<Movie> apply(Integer input) {
                getMovieDetails(input);
                return null;
            }
        });*/
    }

    /*public void setMovieIdLiveData(int movieId) {
        this.movieIdLiveData.postValue(movieId);
    }*/

    public LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }

    public LiveData<List<MovieTrailer>> getTrailerLiveData() {
        return trailerLiveData;
    }

    public LiveData<List<MovieReview>> getReviewsLiveData() {
        return reviewsLiveData;
    }

    public LiveData<Integer> getFavoriteLiveData() {
        return favoriteLiveData;
    }

    public void setFavoriteLiveData(int favorite) {
        this.favoriteLiveData.postValue(favorite);
        Movie movie = movieLiveData.getValue();
        movie.setFavorite(favorite);
        updateMovie(movie);
    }

    public void getMovieDetails(int movieId){
        getLocalMovieById(movieId);
        //getRemoteMovieById(movieId);
        getMovieReviews(movieId);
        getMovieTrailers(movieId);
    }

    private void getLocalMovieById(int movieId){
        movieListRepository.getLocalMovieById(movieId).subscribe(new Observer<Movie>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Movie movie) {
                movieLiveData.postValue(movie);
                Log.d(TAG, movie.getOriginalTitle()+" favorite: "+movie.getFavorite());
                favoriteLiveData.postValue(movie.getFavorite());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getRemoteMovieById(int movieId){
        movieListRepository.getRemoteMovieById(movieId).subscribe(new Observer<Movie>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Movie movie) {
                Log.d(TAG, "remote movie "+movie.getOriginalTitle()+" favorite: "+movie.getFavorite());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getMovieTrailers(int movieId){
        movieListRepository.getMovieTrailers(movieId).subscribe(new Observer<List<MovieTrailer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<MovieTrailer> movieTrailers) {
                trailerLiveData.postValue(movieTrailers);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getMovieReviews(int movieId){
        movieListRepository.getMovieReviews(movieId,1).subscribe(new Observer<List<MovieReview>>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(List<MovieReview> movieReviews) {
                reviewsLiveData.postValue(movieReviews);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void updateMovie(final Movie movie){
        movieListRepository.updateMovie(movie).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Integer integer) {
                if(integer!=-1){
                    movieLiveData.postValue(movie);
                }
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
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
