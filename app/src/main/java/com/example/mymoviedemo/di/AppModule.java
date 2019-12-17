package com.example.mymoviedemo.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {DbModule.class,ApiModule.class})
public class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application){
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences.Editor provideSharedPreferencesEditor(Application context){
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
