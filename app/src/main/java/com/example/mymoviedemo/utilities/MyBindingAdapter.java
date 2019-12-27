package com.example.mymoviedemo.utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymoviedemo.BuildConfig;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.data_fetch.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyBindingAdapter {

    @BindingAdapter(value = {"uri"})
    public static void loadImage(@NonNull ImageView imageView, Uri uri){
        Context context = imageView.getContext();
        if(uri==null) return;
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .error(context.getResources().getDrawable(R.drawable.ic_warning_black_24dp))
                        .placeholder(context.getResources().getDrawable(R.drawable.movie_placeholder)))
                .into(imageView);
    }

    public static Uri createMoviePosterUri(String posterUrl){
        if(posterUrl.equals("")) return null;
        return Uri.parse(BuildConfig.IMAGE_BASE_URL+BuildConfig.POSTER_SIZE+posterUrl);
    }

    public static Uri createMovieBackdropUri(String backdropUrl){
        if(backdropUrl.equals("")) return null;
        return Uri.parse(BuildConfig.IMAGE_BASE_URL+BuildConfig.BACKDROP_SIZE+backdropUrl);
    }

    @BindingAdapter(value = {"favoriteStatus"})
    public static void favoriteStatusToImageRes(@NonNull FloatingActionButton floatingActionButton,int favoriteStatus){
        if(favoriteStatus==1){
            floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @BindingAdapter(value = {"initialLoad","network"})
    public static void statusToVisibility(@NonNull View view, Status initialLoad,Status network ){
        if(initialLoad==null && network==null) {
            view.setVisibility(View.GONE);
            return;
        }else{
            if(initialLoad!=null){
                switch (initialLoad){
                    case LOADING:
                        if(view instanceof ProgressBar){
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    case NO_INTERNET:
                        if(view instanceof LinearLayout){
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        view.setVisibility(View.GONE);
                        break;
                }
            }
            if(network!=null){
                switch (network){
                    case LOADING:
                        if(view instanceof ProgressBar){
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    case NO_INTERNET:
                        if(view instanceof LinearLayout){
                            view.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        view.setVisibility(View.GONE);
                        break;
                }
            }

        }
    }

   /* @BindingAdapter(value = {"initialLoad","network"})
    public static void statusToVisibility(@NonNull LinearLayout layout, Status initialLoad,Status network ){
        if(initialLoad==null && network==null) {
            layout.setVisibility(View.GONE);
            return;
        }else{
            if(initialLoad!=null){
                switch (initialLoad){
                    case NO_INTERNET:
                        layout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        layout.setVisibility(View.GONE);
                        break;
                }
            }
            if(network!=null){
                switch (network){
                    case NO_INTERNET:
                        layout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        layout.setVisibility(View.GONE);
                        break;
                }
            }

        }
    }*/

}
