package com.example.nmaroulis.ui.sign_up;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.MainActivity;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.ActivitySignupBinding;
import com.example.nmaroulis.rest.User;
import com.example.nmaroulis.ui.login.LoginActivity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private Context cxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final Button signupButton = binding.suButton;
        final EditText fname = binding.suFname;
        final EditText lname = binding.suLname;
        final EditText email = binding.suUsername;
        final EditText pwd = binding.suPassword;
        final EditText title = binding.suTitle;
        final EditText residense = binding.suResidence;
        final RadioGroup gender = binding.suGender;

        final RadioButton male_button = binding.radioMale;
        final RadioButton female_button = binding.radioFemale;
        final RadioButton hidden_button = binding.radioOther;


        cxt = this;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                RequestQueue queue = Volley.newRequestQueue(cxt);
                String g;
                Integer gc = gender.getCheckedRadioButtonId();

                if (gc.equals(male_button.getId())){
                    g = "Male";
                }
                else if (gc.equals(female_button.getId())){
                    g = "Female";
                }else{
                    g = "Hidden";
                }
                String url ="http://10.0.2.2:8080/newuser?user=" + email.getText().toString() + "&password=" + pwd.getText().toString() +
                        "&fname=" + fname.getText().toString() + "&lname=" + lname.getText().toString() + "&email=" + email.getText().toString() +
                        "&title=" + title.getText().toString() + "&gender=" + g + "&residense=" + residense.getText().toString() ;  // sign-up url

                HashMap<String, String> signup_data = new HashMap();
                signup_data.put("user",email.getText().toString());
                signup_data.put("password",pwd.getText().toString());
                signup_data.put("fname",fname.getText().toString());
                signup_data.put("lname",lname.getText().toString());
                signup_data.put("email",email.getText().toString());
                signup_data.put("title",title.getText().toString());
                signup_data.put("gender",g);
                signup_data.put("residense",residense.getText().toString());
                JSONObject signup_data_json = new JSONObject(signup_data);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url,signup_data_json ,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.d("SignUp ::","Response is: " + response.toString());

                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // fortwnei to Main
                                        startActivity(intent);

                                        String welcome = fname.getText().toString() + " Η Εγγραφή ήταν Επιτυχής";
                                        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                                        finish();  //Complete and destroy activity once successful

                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("LOGIN :: Error","Error is: " + signup_data_json.toString());

                                Toast.makeText(getApplicationContext(), "Η Εγγραφή απέτυχε, δοκιμάστε ξανά!", Toast.LENGTH_LONG).show();

                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");//"application/json");
                        return params;
                    }
                };

                queue.add(jsonObjectRequest);   // Add the request to the RequestQueue.


            }
        });

    }


}