package com.example.mymoviedemo.di.viewmodel;

import com.example.mymoviedemo.data_fetch.LocalDataSource;
import com.example.mymoviedemo.data_fetch.MovieListRepository;
import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepoModule {
    @Provides
    MovieListRepository provideMovieListRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource){
        return new MovieListRepository(remoteDataSource,localDataSource);
    }
}
