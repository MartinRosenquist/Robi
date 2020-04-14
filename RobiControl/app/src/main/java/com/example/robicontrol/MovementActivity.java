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

import org.w3c.dom.Text;

public class MovementActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movement);

        BottomNavigationView _buttomNavigationView = findViewById(R.id.Menu_Navigation);
        Menu _menu = _buttomNavigationView.getMenu();
        MenuItem _menuitem = _menu.getItem(2);
        _menuitem.setChecked(true);

        _buttomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home_Menu:
                        Intent _intent2 = new Intent(MovementActivity.this, HomeActivity.class);
                        startActivity(_intent2);
                        break;
                    case R.id.Face_Menu:
                        Intent _intent = new Intent(MovementActivity.this, FaceActivity.class);
                        startActivity(_intent);
                        break;
                    case R.id.Movement_Menu:

                        break;
                }

                return false;
            }
        });

        TextView _forward = findViewById(R.id.forward);
        TextView _stop = findViewById(R.id.stop);
        TextView _left = findViewById(R.id.left);
        TextView _right = findViewById(R.id.right);
        TextView _backwards = findViewById(R.id.backwards);

        _forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MovementActivity.this,"Forward", Toast.LENGTH_SHORT).show();

            }
        });

        _stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MovementActivity.this,"Stop", Toast.LENGTH_SHORT).show();

            }
        });

        _left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MovementActivity.this,"Left", Toast.LENGTH_SHORT).show();

            }
        });

        _right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MovementActivity.this,"Right", Toast.LENGTH_SHORT).show();

            }
        });

        _backwards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MovementActivity.this,"Backwards", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
