package com.example.robo_robi.ui.face;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.robo_robi.MainActivity;
import com.example.robo_robi.R;

public class FaceFragment extends Fragment {

    private FaceViewModel faceViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        onResume("Face");
        faceViewModel =
                ViewModelProviders.of(this).get(FaceViewModel.class);
        View root = inflater.inflate(R.layout.fragment_face, container, false);
        final TextView textView = root.findViewById(R.id.text_face);
        faceViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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