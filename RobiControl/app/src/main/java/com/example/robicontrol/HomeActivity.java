package com.example.robicontrol;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        BottomNavigationView _buttomNavigationView = findViewById(R.id.Menu_Navigation);
        Menu _menu = _buttomNavigationView.getMenu();
        MenuItem _menuitem = _menu.getItem(0);
        _menuitem.setChecked(true);

        _buttomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home_Menu:
                        break;
                    case R.id.Face_Menu:
                        Intent _intent = new Intent(HomeActivity.this, FaceActivity.class);
                        startActivity(_intent);
                        break;
                    case R.id.Movement_Menu:
                        Intent _intent2 = new Intent(HomeActivity.this, MovementActivity.class);
                        startActivity(_intent2);
                        break;
                }

                return false;
            }
        });

    }

}
