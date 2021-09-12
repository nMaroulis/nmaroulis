package com.example.nmaroulis.ui.connections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConnectionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Connections fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}