package com.example.mymoviedemo.ui;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
import com.example.mymoviedemo.databinding.MainPageFragmentBinding;
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
    private int movie_sort;
    private MainPageFragmentBinding binding;

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.main_page_fragment, container, false);
        setupViews();
        fetchData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setupViews(){
        mViewModel = ViewModelProviders.of(this,factory).get(MainPageViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setMainVM(mViewModel);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),GridLayoutManager.VERTICAL);
        binding.movieListRv.addItemDecoration(itemDecoration);

        movieAdapter = new MovieAdapter(this);
        binding.movieListRv.setAdapter(movieAdapter);


        /*warningLayout = view.findViewById(R.id.no_internet_warning_layout);
        retryBtn = view.findViewById(R.id.retry_btn);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.retry();
            }
        });


        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);


        loadingProgressBar = view.findViewById(R.id.loading_progress_bar);*/

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
                /*mViewModel.getInitialLoadingState().observe(getViewLifecycleOwner(), new Observer<Status>() {
                    @Override
                    public void onChanged(Status status) {
                        switch (status){
                            case LOADING:
                                binding.loadingProgressBar.setVisibility(View.VISIBLE);
                                binding.noInternetWarningLayout.setVisibility(View.GONE);
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
                });*/
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
