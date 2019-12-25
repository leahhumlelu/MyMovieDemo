package com.example.mymoviedemo.ui;

import android.content.Context;
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
            View view = inflater.inflate(R.layout.movie_detail_item_header,parent,false);
            return new HeaderViewHolder(view);
        }else if(viewType == TRAILER_TYPE_ITEM){
            View view = inflater.inflate(R.layout.movie_trailer_item_view,parent,false);
            return new TrailerViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.movie_review_item_view,parent,false);
            return new ReviewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Object item = data.get(position);
        if(item!=null){
            if (item instanceof String && holder instanceof HeaderViewHolder){
                ((HeaderViewHolder) holder).headerView.setText((String)item);
            }else if(item instanceof MovieTrailer && holder instanceof TrailerViewHolder){
                ((TrailerViewHolder) holder).trailerNameTv.setText(((MovieTrailer) item).getName());
                ((TrailerViewHolder) holder).trailerTypeTv.setText(((MovieTrailer) item).getType());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onClick((MovieTrailer) item);
                    }
                });
            }else if(item instanceof MovieReview && holder instanceof ReviewViewHolder){
                ((ReviewViewHolder) holder).reviewAuthorTv.setText("Written by "+((MovieReview) item).getAuthor());
                ((ReviewViewHolder) holder).reviewContentTv.setText(((MovieReview) item).getContent());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onClick((MovieReview) item);
                    }
                });
            }
        }
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailerList) {
        //if(this.movieTrailers ==null) this.movieTrailers = new ArrayList<>();
        this.movieTrailers = movieTrailerList;
        setData();
    }

    public void setMovieReviews(List<MovieReview> movieReviewList) {
        //if(this.movieReviews ==null) this.movieReviews = new ArrayList<>();
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
        /*if(position==0){

        }else if(position >0 && position <= movieTrailers.size()){

        }
        else if(position == movieTrailers.size()+1){
            return ITEM_TYPE_HEADER;
        }else if(position > movieTrailers.size()+1 && position < data.size()){
            return REVIEW_TYPE_ITEM;
        }*/

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView headerView;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.movie_detail_item_header);
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

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthorTv,reviewContentTv;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthorTv = itemView.findViewById(R.id.movie_detail_review_author);
            reviewContentTv= itemView.findViewById(R.id.movie_detail_review_content);
        }
    }
}
