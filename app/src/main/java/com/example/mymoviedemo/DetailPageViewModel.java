package com.example.mymoviedemo;

import androidx.lifecycle.LiveData;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DetailPageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;

    @Inject
    public DetailPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
    }


    public Observable<List<MovieTrailer>> getMovieTrailers(int movieId){
        return movieListRepository.getMovieTrailers(movieId).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<MovieReview>> getMovieReviews(int movieId){
        return movieListRepository.getMovieReviews(movieId,1);
    }


}
