package com.example.mymoviedemo;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mymoviedemo.model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageFragment extends Fragment {
    private static final String TAG = "MainPageFragment";
    private RecyclerView movieListRv;

    private MainPageViewModel mViewModel;
    private LinearLayout warningLayout;
    private Button retryBtn;

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
                //todo
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showing different contents based on internet connection
        isOnline();
    }

    private void isOnline(){
        if(checkInternetConnection()){
            Log.d(TAG, "isOnline: true");
            warningLayout.setVisibility(View.GONE);
            List<Movie> movieList = new ArrayList<>();
            movieList.add(new Movie("dummy title"));
            movieList.add(new Movie("dummy title2"));
            movieListRv.setAdapter(new MovieAdapter(movieList));
        }else{
            Log.d(TAG, "isOnline: false");
            warningLayout.setVisibility(View.VISIBLE);
        }
        mViewModel = ViewModelProviders.of(this).get(MainPageViewModel.class);
    }

    private boolean checkInternetConnection() {
        /*Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return exitValue==0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;*/
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
