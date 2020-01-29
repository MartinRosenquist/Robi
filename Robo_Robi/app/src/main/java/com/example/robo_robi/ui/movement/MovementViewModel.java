package com.example.robo_robi.ui.movement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovementViewModel extends ViewModel {


    private MutableLiveData<String> mMovement;

    public MovementViewModel() {

        mMovement = new MutableLiveData<>();
        mMovement.setValue("Move Robi with the buttons");
    }


    public LiveData<String> getMovement() {return mMovement;}
}