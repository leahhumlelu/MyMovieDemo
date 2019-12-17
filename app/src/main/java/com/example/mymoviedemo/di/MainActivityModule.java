package com.example.mymoviedemo.di;

import com.example.mymoviedemo.DetailPageViewModel;
import com.example.mymoviedemo.FavoriteMoviesViewModel;
import com.example.mymoviedemo.MainPageViewModel;
import com.example.mymoviedemo.ui.DetailPageFragment;
import com.example.mymoviedemo.ui.FavoriteMoviesFragment;
import com.example.mymoviedemo.ui.MainPageFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = {MainPageViewModel.class})
    abstract MainPageFragment contributeMainPageFragment();

    @ContributesAndroidInjector(modules = {DetailPageViewModel.class})
    abstract DetailPageFragment contributeDetailPageFragment();

    @ContributesAndroidInjector(modules = {FavoriteMoviesViewModel.class})
    abstract FavoriteMoviesFragment contributeFavoriteMoviesFragment();
}
