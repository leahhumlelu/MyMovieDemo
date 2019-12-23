package com.example.mymoviedemo.di.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviedemo.DetailPageViewModel;
import com.example.mymoviedemo.MainPageViewModel;
import com.example.mymoviedemo.ViewModelProviderFactory;

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
}
