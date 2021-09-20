package com.example.nmaroulis.ui.sign_up;

import android.content.Intent;
import android.os.Bundle;

import com.example.nmaroulis.MainActivity;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.ActivitySignupBinding;
import com.example.nmaroulis.ui.login.LoginActivity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Button signupButton = binding.suButton;
        final EditText fname = binding.suFname;
        final EditText lname = binding.suLname;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String welcome = getString(R.string.welcome) + fname.getText().toString() + " " + lname.getText().toString();
                // TODO : initiate successful logged in experience
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();


                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                Log.d("sign up", "success");
            }
        });

    }


}