package com.example.mymoviedemo.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainPageFragment extends Fragment {
    private static final String TAG = "MainPageFragment";
    private RecyclerView movieListRv;
    private NavController navController;
    private LinearLayout warningLayout;
    private Button retryBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        setupViews(view);
    }

    private void setupViews(View view){
        movieListRv = view.findViewById(R.id.movie_list_rv);
        movieListRv.setAdapter(new MovieAdapter(null));
        warningLayout = view.findViewById(R.id.no_internet_warning_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnline();
            }
        });
        //swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showing different contents based on internet connection
        isOnline();
        initScrollListener();
    }


    private void isOnline(){
        if(checkInternetConnection()){
            Log.d(TAG, "isOnline: true");
            warningLayout.setVisibility(View.GONE);

            final MovieAdapter movieAdapter = new MovieAdapter(null);
            movieListRv.setAdapter(movieAdapter);

            mViewModel = ViewModelProviders.of(this,factory).get(MainPageViewModel.class);
            mViewModel.getMovieList(Util.SORT_BY_POPULAR).observe(this, new androidx.lifecycle.Observer<PagedList<Movie>>() {
                @Override
                public void onChanged(PagedList<Movie> moviePagedList) {
                    movieAdapter.submitList(moviePagedList);
                }
            });
            /*mViewModel.getMovieList(Util.SORT_BY_POPULAR).subscribe(new Observer<List<Movie>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<Movie> movies) {

                    movieAdapter.addMovieList(movies);
                    isLoading = false;
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: "+e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });*/



        }else{
            Log.d(TAG, "isOnline: false");
            warningLayout.setVisibility(View.VISIBLE);
        }

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

}
