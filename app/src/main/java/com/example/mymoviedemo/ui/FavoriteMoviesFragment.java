package com.example.mymoviedemo.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymoviedemo.view_model.FavoriteMoviesViewModel;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.view_model.ViewModelProviderFactory;
import com.example.mymoviedemo.model.Movie;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class FavoriteMoviesFragment extends Fragment implements MovieAdapter.ClickListener {

    private FavoriteMoviesViewModel mViewModel;
    private RecyclerView favoriteMovieRv;
    private MovieAdapter movieAdapter;
    private NavController navController;
    @Inject
    public ViewModelProviderFactory factory;


    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_movies_fragment, container, false);
        setUpViews(rootView);
        fetchData();
        return rootView;
    }

    private void fetchData() {
        mViewModel = ViewModelProviders.of(this,factory).get(FavoriteMoviesViewModel.class);

        mViewModel.getMoviePagedList().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                movieAdapter.submitList(movies);
            }
        });
    }

    private void setUpViews(View view) {
        favoriteMovieRv = view.findViewById(R.id.favorite_movie_list_rv);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),GridLayoutManager.VERTICAL);
        favoriteMovieRv.addItemDecoration(itemDecoration);
        movieAdapter = new MovieAdapter(this);
        favoriteMovieRv.setAdapter(movieAdapter);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
    }

    @Override
    public void onClick(Movie movie) {
        FavoriteMoviesFragmentDirections.ActionFavoriteMoviesFragmentToDetailPageFragment action = FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailPageFragment(movie);
        navController.navigate(action);
    }
}
