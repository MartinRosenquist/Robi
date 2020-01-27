package com.example.robo_robi.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    /* our 3 Used Mutable Live data, that we check up on */
    private MutableLiveData<String> connText;
    private MutableLiveData<String> welcomeText;
    private MutableLiveData<Boolean> mBool;


    /* constructor for the home view model */
    public HomeViewModel() {
        /* creating a new mutable live data */
        connText = new MutableLiveData<>();
        welcomeText = new MutableLiveData<>();
        mBool = new MutableLiveData<>();
        /* setting the standard value to fals */
        mBool.setValue(false);

    }

    /* Getting and Setting the ConnText */
    public LiveData<String> getConnText() {
        return connText;
    }

    public LiveData<String> setConnText(){
        if (mBool.getValue()){
            connText.setValue("Robi Now Found");
        }
        else{
            connText.setValue("Robi Not Found");
        }
        return connText;
    }


    /* Getting and Setting the Welcome Text */
    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<String> setWelcomeText(){
        if (mBool.getValue()){
            welcomeText.setValue("You now have access to all of Robi's functions");
        }
        else{
            welcomeText.setValue("Connect to Robi with Bluetooth to use app functionality!");
        }
        return connText;
    }

    /* getting and Setting the boolean that defines if connected or not */
    public LiveData<Boolean> getBool() {
        return mBool;
    }

    public LiveData<Boolean> SetBool(Boolean conn){
        mBool.setValue(conn);
        return mBool;
    }




}