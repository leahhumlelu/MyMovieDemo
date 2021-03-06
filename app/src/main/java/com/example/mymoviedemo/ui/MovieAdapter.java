package com.example.mymoviedemo.ui;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymoviedemo.BuildConfig;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.databinding.MovieItemViewBinding;
import com.example.mymoviedemo.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends PagedListAdapter<Movie,MovieAdapter.MovieViewHolder> {
    private static final String TAG = "MovieAdapter";
    private ClickListener clickListener;

    public interface ClickListener {
        void onClick(Movie movie);
    }

    protected MovieAdapter(ClickListener listener) {
        super(DIFF_CALLBACK);
        this.clickListener = listener;
    }

    private static final DiffUtil.ItemCallback<Movie> DIFF_CALLBACK = new DiffUtil.ItemCallback<Movie>() {
        @Override
        public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
            return (oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getPosterPath().equals(newItem.getPosterPath()));
        }
    };


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MovieItemViewBinding movieItemViewBinding = MovieItemViewBinding.inflate(layoutInflater,parent,false);
        return new MovieViewHolder(movieItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie movie = getItem(position);
        if(movie==null) return;
        holder.bind(movie);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(movie);
            }
        });
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private MovieItemViewBinding binding;
        public MovieViewHolder(@NonNull MovieItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie){
            binding.setMovie(movie);
            binding.executePendingBindings();
        }
    }
}
