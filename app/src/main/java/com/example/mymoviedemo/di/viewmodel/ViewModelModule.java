package com.example.mymoviedemo.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviedemo.view_model.DetailPageViewModel;
import com.example.mymoviedemo.view_model.FavoriteMoviesViewModel;
import com.example.mymoviedemo.view_model.MainPageViewModel;
import com.example.mymoviedemo.view_model.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = {RepoModule.class})
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainPageViewModel.class)
    abstract ViewModel bindMainPageViewModel(MainPageViewModel mainPageViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(DetailPageViewModel.class)
    abstract ViewModel bindDetailPageViewModel(DetailPageViewModel detailPageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesViewModel.class)
    abstract ViewModel bindFavoriteViewModel(FavoriteMoviesViewModel favoriteMoviesViewModel);
}
