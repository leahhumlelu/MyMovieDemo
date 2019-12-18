package com.example.mymoviedemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.mymoviedemo.R;
import com.google.android.material.navigation.NavigationView;

import java.security.Permissions;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {
    private static final String TAG = "MainActivity";
    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private static final int INTERNET_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternetPermission();
    }

    private void checkInternetPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},INTERNET_REQUEST_CODE);
        }else{
            setupNavigation();
        }
    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainPageFragment,R.id.favoriteMoviesFragment)
                .setDrawerLayout(drawerLayout).build();
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case INTERNET_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    setupNavigation();
                }else{
                    checkInternetPermission();
                }
            }
             return;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "onSupportNavigateUp: ");
        return NavigationUI.navigateUp(navController,appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //close drawer when one item get selected
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();

        //switch to different fragment based on selected id
        int id = menuItem.getItemId();
        switch (id){
            case R.id.main:
                navController.navigate(R.id.mainPageFragment);
                break;
            case R.id.favorites:
                navController.navigate(R.id.favoriteMoviesFragment);
                break;
        }
        return false;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
