package com.example.mymoviedemo.data_fetch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mymoviedemo.data_fetch.remote.RemoteDataSource;
import com.example.mymoviedemo.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListRepository {
    private static final String TAG = "MovieListRepository";
    private MutableLiveData<Resource<List<Movie>>> moviesListLivedata = new MutableLiveData<>();
    private RemoteDataSource remoteDataSource;

    public MovieListRepository(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public LiveData<Resource<List<Movie>>> getMovieList(){
        remoteDataSource.getMovieList().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if(response.isSuccessful()){
                    Resource<List<Movie>> movieListResource = Resource.success(response.body());
                    moviesListLivedata.postValue(movieListResource);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Resource<List<Movie>> movieListResource = Resource.error(t.getLocalizedMessage(),null);
                moviesListLivedata.postValue(movieListResource);
            }
        });
        return moviesListLivedata;
    }
}
