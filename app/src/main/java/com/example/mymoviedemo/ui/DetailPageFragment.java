package com.example.mymoviedemo.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
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
import com.example.mymoviedemo.databinding.DetailPageFragmentBinding;
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
    private NavController navController;
    private Toolbar toolbar;
    private Movie fetchedMovie;
    private MovieTrailerReviewAdapter movieTrailerReviewAdapter;
    private MenuItem favoriteBtn2;
    private static final String MOVIE_ARGUMENT_KEY = "movie_argument_key";
    private int movieId;
    private DetailPageFragmentBinding binding;

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
            movieId = savedInstanceState.getInt(MOVIE_ARGUMENT_KEY);
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
        binding = DataBindingUtil.inflate(inflater,R.layout.detail_page_fragment,container,false);
        setupViews(binding.getRoot());
        if(movieId!=0){
            fetchData(movieId);
        }
        return binding.getRoot();
    }

    public void fetchData(int movieId){
        mViewModel.getMovieDetails(movieId);
        mViewModel.getMovieLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                fetchedMovie = movie;
                binding.setMovieDetail(movie);
                updateUi(movie);
            }
        });
        mViewModel.getReviewsLiveData().observe(this, new Observer<List<MovieReview>>() {
            @Override
            public void onChanged(List<MovieReview> movieReviews) {
                movieTrailerReviewAdapter.setMovieReviews(movieReviews);
            }
        });
        mViewModel.getTrailerLiveData().observe(this, new Observer<List<MovieTrailer>>() {
            @Override
            public void onChanged(List<MovieTrailer> movieTrailers) {
                movieTrailerReviewAdapter.setMovieTrailers(movieTrailers);
            }
        });
    }

    private void updateUi(Movie movie) {
        if(movie!=null){
            if(movie.getFavorite()==1){
                favoriteBtn2.setIcon(R.drawable.ic_favorite_black_24dp);
            }else{
                favoriteBtn2.setIcon(R.drawable.ic_favorite_border_black_24dp);
            }

        }
    }


    private void setupViews(View view) {
        mViewModel = ViewModelProviders.of(this,factory).get(DetailPageViewModel.class);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        binding.setDetailVM(mViewModel);
        binding.setLifecycleOwner(this);
        binding.setMovieDetail(null);

        binding.detailToolbar.inflateMenu(R.menu.detail_page_menu);
        favoriteBtn2 = binding.detailToolbar.getMenu().findItem(R.id.favorite_btn_2);
        favoriteBtn2.setVisible(false);

        NavigationUI.setupWithNavController(binding.collapsingToolbarLayout,binding.detailToolbar,navController);

        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) >=binding.appBar.getTotalScrollRange()-56){//appbar is fully collapsed
                    favoriteBtn2.setVisible(true);
                }else{
                    favoriteBtn2.setVisible(false);
                }
            }
        });
        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFavorite();
            }
        });

        binding.detailToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        binding.nestedScrollView.movieDetailTrailerReviewRv.addItemDecoration(itemDecoration);
        movieTrailerReviewAdapter = new MovieTrailerReviewAdapter(getContext(),this);
        binding.nestedScrollView.movieDetailTrailerReviewRv.setAdapter(movieTrailerReviewAdapter);
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
