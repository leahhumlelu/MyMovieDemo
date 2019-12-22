package com.example.mymoviedemo.ui;

import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mymoviedemo.MainPageViewModel;
import com.example.mymoviedemo.R;
import com.example.mymoviedemo.ViewModelProviderFactory;
import com.example.mymoviedemo.model.Movie;
import com.example.mymoviedemo.utilities.Util;

import java.util.List;

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
    @Inject
    public ViewModelProviderFactory factory;
    private MainPageViewModel mViewModel;
    private boolean isLoading = false;

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setupViews(view);
    }

    private void setupViews(View view){
        movieListRv = view.findViewById(R.id.movie_list_rv);
        movieListRv.setAdapter(new MovieAdapter(null));
        //int columns = (int) getResources().getDimension(R.dimen.span_count);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        movieListRv.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),layoutManager.getOrientation());
        movieListRv.addItemDecoration(itemDecoration);
        movieAdapter = new MovieAdapter(this);
        movieListRv.setAdapter(movieAdapter);
        warningLayout = view.findViewById(R.id.no_internet_warning_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnline();
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isOnline();
        //initScrollListener();
    }


    private void isOnline(){
        mViewModel = ViewModelProviders.of(this,factory).get(MainPageViewModel.class);
        mViewModel.getMoviePagedList().observe(this, new Observer<PagedList<Movie>>() {
            @Override
            public void onChanged(PagedList<Movie> movies) {
                movieAdapter.submitList(movies);
            }
        });
        /*if(checkInternetConnection()){
            Log.d(TAG, "isOnline: true");
            warningLayout.setVisibility(View.GONE);

        }else{
            Log.d(TAG, "isOnline: false");
            warningLayout.setVisibility(View.VISIBLE);
        }*/

    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initScrollListener() {
        movieListRv.addOnScrollListener(new EndlessScrollEventListener() {
            @Override
            public void onLoadMore() {
                mViewModel.getMovieList(Util.SORT_BY_POPULAR);
            }
        });
    }

    @Override
    public void onClick(Movie movie) {
        //navController.navigate(R.id.action_mainPageFragment_to_detailPageFragment);
        MainPageFragmentDirections.ActionMainPageFragmentToDetailPageFragment action =
                MainPageFragmentDirections.actionMainPageFragmentToDetailPageFragment(movie);
        navController.navigate(action);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_page_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_page_movie_sort:
                if(item.getTitle().equals(getResources().getString(R.string.sort_popular))){
                    item.setTitle(getResources().getString(R.string.sort_top_rate));
                    mViewModel.setSortLiveData(Util.SORT_BY_TOP_RATED);
                }else{
                    item.setTitle(getResources().getString(R.string.sort_popular));
                    mViewModel.setSortLiveData(Util.SORT_BY_POPULAR);
                }

        }
        return super.onOptionsItemSelected(item);
    }
}
