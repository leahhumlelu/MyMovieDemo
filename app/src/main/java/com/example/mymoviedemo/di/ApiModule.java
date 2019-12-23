package com.example.mymoviedemo.di;

import android.app.Application;

import com.example.mymoviedemo.BuildConfig;
import com.example.mymoviedemo.data_fetch.remote.MovieApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    @Provides
    @Singleton
    Cache provideCache(Application application){
        long cacheSize = 10 * 1024 * 1024; // 10MB
        File httpCacheDirectory = new File(application.getCacheDir(),"http_cache");
        return new Cache(httpCacheDirectory,cacheSize);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, ConnectivityInterceptor connectivityInterceptor,RequestInterceptor requestInterceptor){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.cache(cache)
                .addInterceptor(logging)
                .addInterceptor(connectivityInterceptor)
                .addNetworkInterceptor(requestInterceptor);
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient){
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    MovieApiInterface provideMovieApiInterface(Retrofit retrofit){
        return retrofit.create(MovieApiInterface.class);

    }

}
