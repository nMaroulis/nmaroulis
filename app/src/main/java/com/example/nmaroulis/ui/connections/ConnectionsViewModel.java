package com.example.nmaroulis.ui.connections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConnectionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Λίστα Συνδέσεων");

    }

    public LiveData<String> getText() {
        return mText;
    }

}