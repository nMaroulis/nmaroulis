package com.example.nmaroulis.ui.profile;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.rest.User;

import java.io.IOException;


public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");

        //User user = new User();
        //user.setUsername("emily");
        //new UserDetailsRequestTask().execute(user.getId());

    }

    public LiveData<String> getText() {
        return mText;
    }


}