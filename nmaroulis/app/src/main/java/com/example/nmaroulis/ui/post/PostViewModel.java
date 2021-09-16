package com.example.nmaroulis.ui.post;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nmaroulis.ConnectionHelper;
import com.example.nmaroulis.ui.connections.ConnectionsFragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Δημιουργία Καινούργιας Ανάρτησης");
    }

    public LiveData<String> getText() {
        return mText;
    }
}