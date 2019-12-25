package com.example.mymoviedemo.di;

import com.example.mymoviedemo.di.viewmodel.ViewModelModule;
import com.example.mymoviedemo.ui.DetailPageFragment;
import com.example.mymoviedemo.ui.FavoriteMoviesFragment;
import com.example.mymoviedemo.ui.MainPageFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = {ViewModelModule.class})
    abstract MainPageFragment contributeMainPageFragment();

    @ContributesAndroidInjector(modules = {ViewModelModule.class})
    abstract DetailPageFragment contributeDetailPageFragment();

    @ContributesAndroidInjector(modules = {ViewModelModule.class})
    abstract FavoriteMoviesFragment contributeFavoriteMoviesFragment();
}
