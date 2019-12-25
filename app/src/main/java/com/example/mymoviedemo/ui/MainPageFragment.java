package com.example.mymoviedemo.ui;

import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mymoviedemo.data_fetch.Status;
import com.example.mymoviedemo.view_model.MainPageViewModel;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.view_model.ViewModelProviderFactory;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MainPageFragment extends Fragment implements MovieAdapter.ClickListener {
    private static final String TAG = "MainPageFragment";
    private RecyclerView movieListRv;
    private NavController navController;
    private LinearLayout warningLayout;
    private Button retryBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter movieAdapter;
    private ProgressBar loadingProgressBar;
    @Inject
    public ViewModelProviderFactory factory;
    private MainPageViewModel mViewModel;
    private static final String PREFERENCE_SORT_KEY = "preference_sort_key";
    private int movie_sort;

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        /*if(savedInstanceState!=null){
            movie_sort = savedInstanceState.getInt(PREFERENCE_SORT_KEY);
        }else{
            movie_sort = Util.SORT_BY_POPULAR;
        }*/
    }

  /*  @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PREFERENCE_SORT_KEY,movie_sort);
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_page_fragment, container, false);
        setupViews(rootView);
        fetchData();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setupViews(View view){
        mViewModel = ViewModelProviders.of(this,factory).get(MainPageViewModel.class);
        movieListRv = view.findViewById(R.id.movie_list_rv);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),GridLayoutManager.VERTICAL);
        movieListRv.addItemDecoration(itemDecoration);
        movieAdapter = new MovieAdapter(this);
        movieListRv.setAdapter(movieAdapter);
        warningLayout = view.findViewById(R.id.no_internet_warning_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.retry();
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar);

    }


    private void fetchData(){
        mViewModel.getSortLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                movie_sort = integer;
                mViewModel.getMovieList(integer).observe(getViewLifecycleOwner(), new Observer<PagedList<Movie>>() {
                    @Override
                    public void onChanged(PagedList<Movie> moviePagedList) {
                        movieAdapter.submitList(moviePagedList);
                    }
                });
                mViewModel.getInitialLoadingState().observe(getViewLifecycleOwner(), new Observer<Status>() {
                    @Override
                    public void onChanged(Status status) {
                        switch (status){
                            case LOADING:
                                loadingProgressBar.setVisibility(View.VISIBLE);
                                warningLayout.setVisibility(View.GONE);
                                break;
                            case NO_INTERNET:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.VISIBLE);
                                break;
                            case SUCCESS:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.GONE);
                                break;
                            case ERROR:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.GONE);
                                Toast.makeText(getContext(),getResources().getString(R.string.error_fetching_data),Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                mViewModel.getNetworkState().observe(getViewLifecycleOwner(), new Observer<Status>() {
                    @Override
                    public void onChanged(Status status) {
                        switch (status){
                            case LOADING:
                                loadingProgressBar.setVisibility(View.VISIBLE);
                                warningLayout.setVisibility(View.GONE);
                                break;
                            case NO_INTERNET:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.VISIBLE);
                                break;
                            case SUCCESS:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.GONE);
                                break;
                            case ERROR:
                                loadingProgressBar.setVisibility(View.GONE);
                                warningLayout.setVisibility(View.GONE);
                                Toast.makeText(getContext(),getResources().getString(R.string.error_fetching_data),Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(Movie movie) {
        MainPageFragmentDirections.ActionMainPageFragmentToDetailPageFragment action =
                MainPageFragmentDirections.actionMainPageFragmentToDetailPageFragment(movie.getId());
        navController.navigate(action);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu: ");
        MenuItem item = menu.findItem(R.id.main_page_movie_sort);
        if(movie_sort==Util.SORT_BY_POPULAR){
            item.setTitle(getResources().getString(R.string.sort_top_rate));
        }else{
            item.setTitle(getResources().getString(R.string.sort_popular));
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_page_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        switch (item.getItemId()){
            case R.id.main_page_movie_sort:
                if(item.getTitle().equals(getResources().getString(R.string.sort_popular))){
                    item.setTitle(getResources().getString(R.string.sort_top_rate));
                    movie_sort = Util.SORT_BY_POPULAR;
                }else{
                    movie_sort= Util.SORT_BY_TOP_RATED;
                    item.setTitle(getResources().getString(R.string.sort_popular));
                }
                mViewModel.setSortLiveData(movie_sort);

        }
        return super.onOptionsItemSelected(item);
    }
}
