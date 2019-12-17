package com.example.mymoviedemo.di;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {
    private Context context;

    @Inject
    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //todo
        /*if(!Utilities.isOnline(context)){
            //user is not online
            //todo
            throw new IOException();
        }*/
        return chain.proceed(chain.request());
    }
}
