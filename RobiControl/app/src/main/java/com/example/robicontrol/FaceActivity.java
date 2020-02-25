package com.example.robicontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FaceActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face);

        BottomNavigationView _buttomNavigationView = findViewById(R.id.Menu_Navigation);
        Menu _menu = _buttomNavigationView.getMenu();
        MenuItem _menuitem = _menu.getItem(1);
        _menuitem.setChecked(true);

        _buttomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home_Menu:
                        Intent _intent2 = new Intent(FaceActivity.this, HomeActivity.class);
                        startActivity(_intent2);
                        break;
                    case R.id.Face_Menu:

                        break;
                    case R.id.Movement_Menu:
                        Intent _intent = new Intent(FaceActivity.this, MovementActivity.class);
                        startActivity(_intent);
                        break;
                }

                return false;
            }
        });

        TextView _happy_face = findViewById(R.id.happy_face);
        TextView _sad_face = findViewById(R.id.sad_face);
        TextView _angry_face = findViewById(R.id.angry_face);
        TextView _neutral_face = findViewById(R.id.neutral_face);

        _happy_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaceActivity.this,"Happy Face",Toast.LENGTH_SHORT).show();
            }
        });

        _sad_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaceActivity.this,"Sad Face",Toast.LENGTH_SHORT).show();
            }
        });

        _angry_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaceActivity.this,"Angry Face",Toast.LENGTH_SHORT).show();
            }
        });

        _neutral_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FaceActivity.this,"Neutral Face",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
