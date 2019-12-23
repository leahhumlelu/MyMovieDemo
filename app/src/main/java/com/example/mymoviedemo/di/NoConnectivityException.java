package com.example.mymoviedemo.di;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "NO connectivity exception";
    }
}
