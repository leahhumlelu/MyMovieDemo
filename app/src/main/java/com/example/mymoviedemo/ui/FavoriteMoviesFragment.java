package com.example.mymoviedemo.ui;

import androidx.databinding.DataBindingUtil;
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

import com.example.mymoviedemo.databinding.FavoriteMoviesFragmentBinding;
import com.example.mymoviedemo.view_model.FavoriteMoviesViewModel;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.view_model.ViewModelProviderFactory;
import com.example.mymoviedemo.model.Movie;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class FavoriteMoviesFragment extends Fragment implements MovieAdapter.ClickListener {

    private FavoriteMoviesViewModel mViewModel;
    private MovieAdapter movieAdapter;
    private NavController navController;
    @Inject
    public ViewModelProviderFactory factory;
    private FavoriteMoviesFragmentBinding binding;
    private static final String SCROLL_POSITION_KEY ="scroll_position_key";
    private int scrollPosition;


    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_movies_fragment,container,false);
        setUpViews();
        fetchData();
        return binding.getRoot();
    }


    private void setUpViews() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),GridLayoutManager.VERTICAL);
        binding.favoriteMovieListRv.addItemDecoration(itemDecoration);
        movieAdapter = new MovieAdapter(this);
        binding.favoriteMovieListRv.setAdapter(movieAdapter);
        binding.favoriteMovieListRv.scrollToPosition(scrollPosition);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        mViewModel = ViewModelProviders.of(this,factory).get(FavoriteMoviesViewModel.class);
        binding.setFavoriteVM(mViewModel);
        binding.setLifecycleOwner(this);
    }

    private void fetchData() {
        mViewModel.getMoviePagedList().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                movieAdapter.submitList(movies);
            }
        });
    }

    @Override
    public void onClick(Movie movie) {
        FavoriteMoviesFragmentDirections.ActionFavoriteMoviesFragmentToDetailPageFragment action = FavoriteMoviesFragmentDirections.actionFavoriteMoviesFragmentToDetailPageFragment(movie.getId());
        navController.navigate(action);
    }
}
