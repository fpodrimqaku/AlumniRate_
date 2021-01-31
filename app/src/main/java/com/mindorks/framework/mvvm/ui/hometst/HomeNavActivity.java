package com.mindorks.framework.mvvm.ui.hometst;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mindorks.framework.mvvm.R;

public class HomeNavActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view_custom);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_custom, R.id.navigation_dashboard_custom, R.id.navigation_notifications_custom)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_custom);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //androidx.navigation.fragment
    }
}
