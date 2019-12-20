package com.example.mymoviedemo.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mymoviedemo.DetailPageViewModel;
import com.example.mymoviedemo.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailPageFragment extends Fragment {
    private static final String TAG = "DetailPageFragment";
    private DetailPageViewModel mViewModel;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FloatingActionButton floatingActionButton;
    private NavController navController;
    private Toolbar toolbar;

    public static DetailPageFragment newInstance() {
        return new DetailPageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(favoriteBtnEnabled);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_page_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int movieId = DetailPageFragmentArgs.fromBundle(getArguments()).getMovieId();
        setupCollapsingAppbar(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DetailPageViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setupCollapsingAppbar(View view) {
        navController = Navigation.findNavController(view);
        toolbar = view.findViewById(R.id.detail_toolbar);
        toolbar.inflateMenu(R.menu.detail_page_menu);
        final MenuItem favoriteBtn2 = toolbar.getMenu().findItem(R.id.favorite_btn_2);
        favoriteBtn2.setVisible(false);
        appBarLayout = view.findViewById(R.id.app_bar);
        floatingActionButton = view.findViewById(R.id.favorite_btn);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("dummy title");

        NavigationUI.setupWithNavController(collapsingToolbarLayout,toolbar,navController);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.i(TAG, "onOffsetChanged: veritical offset: "+verticalOffset);
                if(Math.abs(verticalOffset) >=appBarLayout.getTotalScrollRange()-56){//appbar is fully collapsed
                    favoriteBtn2.setVisible(true);
                }else{
                    favoriteBtn2.setVisible(false);
                }
                //setHasOptionsMenu(favoriteBtnEnabled);
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


    }


/*

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.detail_page_menu,menu);
    }*/



    private void switchFavorite() {
    }
}
