package com.example.mymoviedemo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviedemo.R;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {
    private List<MovieReview> movieReviewList;
    private ClickListener clickListener;

    public MovieReviewAdapter(ClickListener clickListener) {
        movieReviewList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(MovieReview movieReview);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_item_view,parent,false);
        return new ReviewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final MovieReview movieReview = movieReviewList.get(position);
        if(movieReview!=null){
            holder.reviewAuthorTv.setText("Written by " + movieReview.getAuthor());
            holder.reviewContentTv.setText(movieReview.getContent());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(movieReview);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movieReviewList==null || movieReviewList.isEmpty()) return 0;
        return movieReviewList.size();
    }

    public void setMovieReviewList(List<MovieReview> movieReviewList) {
        if(this.movieReviewList!=movieReviewList && !movieReviewList.isEmpty()){
            this.movieReviewList = movieReviewList;
            notifyDataSetChanged();
        }

    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthorTv,reviewContentTv;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthorTv = itemView.findViewById(R.id.movie_detail_review_author);
            reviewContentTv= itemView.findViewById(R.id.movie_detail_review_content);
        }
    }
}
