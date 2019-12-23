
package com.example.mymoviedemo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieReviewResult {
    @SerializedName("id")
    private Long mId;
    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<MovieReview> mMovieReviews;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public List<MovieReview> getResults() {
        return mMovieReviews;
    }

    public void setResults(List<MovieReview> movieReviews) {
        mMovieReviews = movieReviews;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
