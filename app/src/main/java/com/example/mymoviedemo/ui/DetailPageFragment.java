package com.example.mymoviedemo.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mymoviedemo.BuildConfig;
import com.example.mymoviedemo.view_model.DetailPageViewModel;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.view_model.ViewModelProviderFactory;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.model.MovieReview;
import com.example.mymoviedemo.model.MovieTrailer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class DetailPageFragment extends Fragment implements MovieTrailerReviewAdapter.ClickListener  { //MovieTrailerAdapter.ClickListener, MovieReviewAdapter.ClickListener
    private static final String TAG = "DetailPageFragment";
    private DetailPageViewModel mViewModel;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView movieIv,moviePosterIv;
    private TextView movieOverview,movieRateTv,movieReleaseDateTv;
    private FloatingActionButton floatingActionButton;
    private NavController navController;
    private Toolbar toolbar;
    private Movie fetchedMovie;
    private RecyclerView movieTrailersRv,movieReviewRv,movieTrailerAndReviewRv;
    private MovieTrailerReviewAdapter movieTrailerReviewAdapter;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;
    private MenuItem favoriteBtn2;
    private static final String MOVIE_ARGUMENT_KEY = "movie_argument_key";
    private int movieId;

    @Inject
    public ViewModelProviderFactory factory;

    public static DetailPageFragment newInstance() {
        return new DetailPageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            movieId = DetailPageFragmentArgs.fromBundle(getArguments()).getMovieId();
        }else{
            movieId = savedInstanceState.getParcelable(MOVIE_ARGUMENT_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVIE_ARGUMENT_KEY,movieId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_page_fragment, container, false);
        setupViews(rootView);
        if(movieId!=0){
            mViewModel = ViewModelProviders.of(this,factory).get(DetailPageViewModel.class);
            mViewModel.getMovieDetails(movieId);
            mViewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
                @Override
                public void onChanged(Movie movie) {
                    fetchedMovie = movie;
                    updateUi(movie);
                }
            });
            mViewModel.getReviewsLiveData().observe(this, new Observer<List<MovieReview>>() {
                @Override
                public void onChanged(List<MovieReview> movieReviews) {
                    //movieReviewAdapter.setMovieReviews(movieReviews);
                    movieTrailerReviewAdapter.setMovieReviews(movieReviews);
                }
            });
            mViewModel.getTrailerLiveData().observe(this, new Observer<List<MovieTrailer>>() {
                @Override
                public void onChanged(List<MovieTrailer> movieTrailers) {
                    //movieTrailerAdapter.setMovieTrailers(movieTrailers);
                    movieTrailerReviewAdapter.setMovieTrailers(movieTrailers);
                }
            });
        }
        return rootView;
    }

    private void updateUi(Movie movie) {
        if(movie!=null){
            collapsingToolbarLayout.setTitle(movie.getOriginalTitle());
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

            Uri movieBackdropUri = Uri.parse(BuildConfig.IMAGE_BASE_URL+BuildConfig.BACKDROP_SIZE+movie.getBackdropPath());
            Log.i(TAG, "BACKGROUP URI "+movieBackdropUri);
            Glide.with(getContext()).load(movieBackdropUri).into(movieIv);

            movieOverview.setText(movie.getOverview());

            Uri moviePosterUri = Uri.parse(BuildConfig.IMAGE_BASE_URL+BuildConfig.POSTER_SIZE+movie.getPosterPath());
            Log.i(TAG, "poster URI "+moviePosterUri);
            Glide.with(getContext()).load(moviePosterUri).into(moviePosterIv);

            movieRateTv.setText(String.valueOf(movie.getVoteAverage()));
            movieReleaseDateTv.setText(movie.getReleaseDate());

            if(movie.getFavorite()==1){
                floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                favoriteBtn2.setIcon(R.drawable.ic_favorite_black_24dp);
            }else{
                floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                favoriteBtn2.setIcon(R.drawable.ic_favorite_border_black_24dp);
            }

        }
    }


    private void setupViews(View view) {
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        toolbar = view.findViewById(R.id.detail_toolbar);

        toolbar.inflateMenu(R.menu.detail_page_menu);

        favoriteBtn2 = toolbar.getMenu().findItem(R.id.favorite_btn_2);
        favoriteBtn2.setVisible(false);

        appBarLayout = view.findViewById(R.id.app_bar);
        floatingActionButton = view.findViewById(R.id.favorite_btn);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        movieIv = view.findViewById(R.id.movie_image);
        movieOverview = view.findViewById(R.id.movie_overview);
        moviePosterIv = view.findViewById(R.id.movie_detail_poster_iv);
        movieRateTv = view.findViewById(R.id.rate_tv);
        movieReleaseDateTv = view.findViewById(R.id.release_date_tv);
        NavigationUI.setupWithNavController(collapsingToolbarLayout,toolbar,navController);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) >=appBarLayout.getTotalScrollRange()-56){//appbar is fully collapsed
                    favoriteBtn2.setVisible(true);
                }else{
                    favoriteBtn2.setVisible(false);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFavorite();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite_btn_2:
                        switchFavorite();
                        return true;
                }
                return false;
            }
        });

        /*movieTrailersRv = view.findViewById(R.id.movie_detail_trailers_rv);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        movieTrailersRv.addItemDecoration(itemDecoration);
        movieTrailerAdapter = new MovieTrailerAdapter(this);
        movieTrailersRv.setAdapter(movieTrailerAdapter);

        movieReviewRv = view.findViewById(R.id.movie_detail_reviews_rv);
        movieReviewRv.addItemDecoration(itemDecoration);
        movieReviewAdapter = new MovieReviewAdapter(this);
        movieReviewRv.setAdapter(movieReviewAdapter);*/


        movieTrailerAndReviewRv = view.findViewById(R.id.movie_detail_trailer_review_rv);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        movieTrailerAndReviewRv.addItemDecoration(itemDecoration);
        movieTrailerReviewAdapter = new MovieTrailerReviewAdapter(getContext(),this);
        movieTrailerAndReviewRv.setAdapter(movieTrailerReviewAdapter);

    }


    private void switchFavorite() {
        if(fetchedMovie.getFavorite()==1){
            mViewModel.setFavoriteLiveData(0);
        }else{
            mViewModel.setFavoriteLiveData(1);
        }
    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        //todo: open a dialog to play from browser or youtube
        Uri trailerUri = Uri.parse(BuildConfig.YOUTUBE_BASE_URL+movieTrailer.getKey());
        Intent intent = new Intent(Intent.ACTION_VIEW,trailerUri);
        Intent chooser = Intent.createChooser(intent,getResources().getString(R.string.intent_title));
        if(chooser.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(chooser);
        }
    }

    @Override
    public void onClick(MovieReview movieReview) {
        Uri reviewUri = Uri.parse(movieReview.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW,reviewUri);
        if(intent.resolveActivity(getActivity().getPackageManager())!=null){
            startActivity(intent);
        }
    }
}
