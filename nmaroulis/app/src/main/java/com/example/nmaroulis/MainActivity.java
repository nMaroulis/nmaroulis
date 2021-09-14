package com.example.nmaroulis;

import android.os.Bundle;

import com.example.nmaroulis.databinding.ToolbarBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.example.nmaroulis.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
         //       R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
         //       .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        Toolbar toolbar = findViewById(R.id.mytoolbar);
        //setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(binding.mytoolbar.toolbar, navController);
        //toolbar.getMenu().clear();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);

        inflater.inflate(R.menu.top_menu, menu);


        return true;
    }


}