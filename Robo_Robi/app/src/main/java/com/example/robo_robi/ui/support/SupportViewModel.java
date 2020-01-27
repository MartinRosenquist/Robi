package com.example.robo_robi.ui.support;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SupportViewModel extends ViewModel {

    private MutableLiveData<String> mTicket;
    private MutableLiveData<String> mBusiness;
    private MutableLiveData<String> mInfo;

    public SupportViewModel() {
        mTicket = new MutableLiveData<>();
        mTicket.setValue("Submit a Ticket");

        mBusiness = new MutableLiveData<>();
        mBusiness.setValue("Contact for business inquiries");

        mInfo = new MutableLiveData<>();
        mInfo.setValue("Info about Robi");
    }

    public LiveData<String> getTicket() {
        return mTicket;
    }
    public LiveData<String> getBusiness() { return mBusiness;}
    public LiveData<String> getInfo() {return mInfo;}
}