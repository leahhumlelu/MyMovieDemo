package com.example.mymoviedemo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviedemo.R;
import com.example.mymoviedemo.databinding.MovieDetailItemHeaderBinding;
import com.example.mymoviedemo.databinding.MovieReviewItemViewBinding;
import com.example.mymoviedemo.databinding.MovieTrailerItemViewBinding;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int TRAILER_TYPE_ITEM = 1;
    public static final int REVIEW_TYPE_ITEM = 2;
    private List<MovieTrailer> movieTrailers;
    private List<MovieReview> movieReviews;
    private List<Object> data;
    private Context context;
    private ClickListener clickListener;


    public interface ClickListener {
        void onClick(MovieReview movieReview);
        void onClick(MovieTrailer movieTrailer);
    }

    public MovieTrailerReviewAdapter(Context context,ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.movieTrailers = new ArrayList<>();
        this.movieReviews = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==ITEM_TYPE_HEADER){
            MovieDetailItemHeaderBinding binding = MovieDetailItemHeaderBinding.inflate(inflater,parent,false);
            return new HeaderViewHolder(binding);
        }else if(viewType == TRAILER_TYPE_ITEM){
            MovieTrailerItemViewBinding binding = MovieTrailerItemViewBinding.inflate(inflater,parent,false);
            return new TrailerViewHolder(binding);
        }else{
            MovieReviewItemViewBinding binding = MovieReviewItemViewBinding.inflate(inflater,parent,false);
            return new ReviewViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Object item = data.get(position);
        if(item!=null){
            if (item instanceof String && holder instanceof HeaderViewHolder){
                ((HeaderViewHolder) holder).bind((String) item);
            }else if(item instanceof MovieTrailer && holder instanceof TrailerViewHolder){
                ((TrailerViewHolder) holder).bind((MovieTrailer) item);
                ((TrailerViewHolder) holder).binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onClick((MovieTrailer) item);
                    }
                });
            }else if(item instanceof MovieReview && holder instanceof ReviewViewHolder){
                ((ReviewViewHolder) holder).bind((MovieReview) item);;
                ((ReviewViewHolder) holder).binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onClick((MovieReview) item);
                    }
                });
            }
        }
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailerList) {
        this.movieTrailers = movieTrailerList;
        setData();
    }

    public void setMovieReviews(List<MovieReview> movieReviewList) {
        this.movieReviews = movieReviewList;
        setData();
    }

    public void setData() {
        data = new ArrayList<>();
        data.add(context.getResources().getString(R.string.trailer_label));
        if(!this.movieTrailers.isEmpty()){
            data.addAll(this.movieTrailers);
        }
        data.add(context.getResources().getString(R.string.review_lable));
        if(!this.movieReviews.isEmpty()){
            data.addAll(this.movieReviews);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(data==null) return 0;
        return data.size(); //plus two headers
    }

    @Override
    public int getItemViewType(int position) {
        final Object item = data.get(position);
        if(item!=null){
            if(item instanceof String){
                return ITEM_TYPE_HEADER;
            }else if(item instanceof MovieTrailer){
                return TRAILER_TYPE_ITEM;
            }else if(item instanceof MovieReview){
                return REVIEW_TYPE_ITEM;
            }

        }
        return super.getItemViewType(position);

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        MovieDetailItemHeaderBinding binding;
        public HeaderViewHolder(@NonNull MovieDetailItemHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String header){
            binding.setHeader(header);
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        MovieTrailerItemViewBinding binding;
        public TrailerViewHolder(@NonNull MovieTrailerItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MovieTrailer trailer){
            binding.setTrailer(trailer);
        }

    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        MovieReviewItemViewBinding binding;
        public ReviewViewHolder(@NonNull MovieReviewItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MovieReview review){
            binding.setReview(review);
        }

    }
}
