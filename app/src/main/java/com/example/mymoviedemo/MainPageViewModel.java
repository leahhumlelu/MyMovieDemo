package com.example.mymoviedemo;

import androidx.lifecycle.ViewModel;

import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.model.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainPageViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MovieListRepository movieListRepository;
    private int page = 1;

    @Inject
    public MainPageViewModel(MovieListRepository movieListRepository) {
        this.movieListRepository = movieListRepository;
    }

    public Observable<List<Movie>> getMovieList(int sort){
        return movieListRepository.getMovieList(sort,page++).observeOn(AndroidSchedulers.mainThread());
    }
}
