package com.example.mymoviedemo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviedemo.R;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieListResult;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = "MovieAdapter";
    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_view,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.textView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if(movieList==null) return 0;
        return movieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        if(this.movieList!=movieList){
            this.movieList = movieList;
            notifyDataSetChanged();
        }
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
