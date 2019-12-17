package com.example.mymoviedemo.data_fetch;

import androidx.annotation.Nullable;

public class Resource<T> {
    Status status;
    T data;
    String msg;

    private Resource(Status status, @Nullable T data, @Nullable String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Resource<T> success(T data){
        return new Resource(Status.SUCCESS,data,null);
    }

    public static <T> Resource<T> error(String msg,@Nullable T data){
        return new Resource(Status.ERROR,data,msg);
    }

    public static <T> Resource<T> loading(@Nullable T data){
        return new Resource(Status.LOADING,data,null);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}