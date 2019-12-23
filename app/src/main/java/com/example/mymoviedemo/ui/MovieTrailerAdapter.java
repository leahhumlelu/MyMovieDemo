package com.example.mymoviedemo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviedemo.R;
import com.example.mymoviedemo.model.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder> {
    private List<MovieTrailer> movieTrailerList;
    private ClickListener clickListener;

    public MovieTrailerAdapter(ClickListener clickListener) {
        movieTrailerList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(MovieTrailer movieTrailer);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_item_view,parent,false);
        return new TrailerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        final MovieTrailer movieTrailer = movieTrailerList.get(position);
        if(movieTrailer!=null){
            holder.trailerNameTv.setText(movieTrailer.getName());
            holder.trailerTypeTv.setText(movieTrailer.getType());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(movieTrailer);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movieTrailerList==null || movieTrailerList.isEmpty()) return 0;
        return movieTrailerList.size();
    }

    public void setMovieTrailerList(List<MovieTrailer> movieTrailerList) {
        if(this.movieTrailerList!=movieTrailerList && !movieTrailerList.isEmpty()){
            this.movieTrailerList = movieTrailerList;
            notifyDataSetChanged();
        }

    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView trailerTypeTv,trailerNameTv;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerTypeTv = itemView.findViewById(R.id.movie_detail_trailer_type);
            trailerNameTv = itemView.findViewById(R.id.movie_detail_trailer_name);
        }
    }
}
