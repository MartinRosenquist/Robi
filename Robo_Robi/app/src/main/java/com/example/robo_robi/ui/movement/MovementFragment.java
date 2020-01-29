package com.example.robo_robi.ui.movement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.robo_robi.MainActivity;
import com.example.robo_robi.R;
import com.example.robo_robi.ui.info.InfoFragment;


public class MovementFragment extends Fragment {

    private MovementViewModel supportViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        supportViewModel =
                ViewModelProviders.of(this).get(MovementViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_movement, container, false);
        onResume("Movement");

        final TextView movementTextView = root.findViewById(R.id.text_movement);



        supportViewModel.getMovement().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String movement) {
                movementTextView.setText(movement);
            }
        });



        return root;
    }



    private void onResume(String title){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(title);

    }
}