
package com.example.mymoviedemo.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieTrailerResult {
    @SerializedName("id")
    private Long id;
    @SerializedName("results")
    @Embedded
    private List<MovieTrailer> movieTrailers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public List<MovieTrailer> getMovieTrailers() {
        return movieTrailers;
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailers) {
        movieTrailers = movieTrailers;
    }

}
