package com.example.mymoviedemo.di;

import android.content.Context;

import com.example.mymoviedemo.utilities.Util;

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

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if(!Util.isOnline(context)){
            throw new NoConnectivityException();
        }
        return chain.proceed(chain.request());
    }
}
