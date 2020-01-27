package com.example.robo_robi.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.robo_robi.MainActivity;
import com.example.robo_robi.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        onResume("Home");
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView connTextView = root.findViewById(R.id.Connection);
        connTextView.setText(homeViewModel.setConnText().getValue());
        homeViewModel.getConnText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                connTextView.setText(s);
            }
        });
        final TextView welcomeTextView = root.findViewById(R.id.Welcome_Message);
        welcomeTextView.setText(homeViewModel.setWelcomeText().getValue());
        homeViewModel.getWelcomeText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                welcomeTextView.setText(s);
            }
        });

        final Button btnClick;

        btnClick = root.findViewById(R.id.testingButton);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateLiveData();

            }
        });



        return root;
    }

    // updates livedata from homeViewModel
    public void updateLiveData(){

        Boolean value =  homeViewModel
                .getBool()
                .getValue();



        if(value == true) {
                homeViewModel
                        .SetBool(false);
        }

        if(value != true){
            homeViewModel
                    .SetBool(true);

        }

        homeViewModel.setConnText();
        homeViewModel.setWelcomeText();


        /*Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();*/

    }

    private void onResume(String title){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(title);

    }


}