package com.example.mymoviedemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieDetailResult;

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

    public Observable<MovieDetailResult> getMovieDetailById(int movieId){
        return movieListRepository.getMovieById(movieId).observeOn(AndroidSchedulers.mainThread());
    }
}
