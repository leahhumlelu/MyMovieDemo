package com.example.mymoviedemo.di;

import android.app.Application;

import androidx.room.Room;

import com.example.mymoviedemo.data_fetch.local.MovieDao;
import com.example.mymoviedemo.data_fetch.local.MovieDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Provides
    @Singleton
    MovieDatabase provideMovieDatabase(Application application){
       return Room.databaseBuilder(application, MovieDatabase.class, "movie_db")
               .build();
    }

    @Provides
    @Singleton
    MovieDao provideMovieDao(MovieDatabase movieDatabase){
        return movieDatabase.getMovieDao();
    }
}
