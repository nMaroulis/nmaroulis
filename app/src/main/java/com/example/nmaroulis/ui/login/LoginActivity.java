package com.example.nmaroulis.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.MainActivity;
import com.example.nmaroulis.R;
import com.example.nmaroulis.rest.User;
import com.example.nmaroulis.ui.login.LoginViewModel;
import com.example.nmaroulis.ui.login.LoginViewModelFactory;
import com.example.nmaroulis.databinding.ActivityLoginBinding;
import com.example.nmaroulis.ui.sign_up.SignUpActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private Context cxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cxt = this;
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final Button signupButton = binding.signup;


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                Gson gson = new Gson();

                Editable login_user = binding.username.getText();
                Editable login_pass = binding.password.getText();

                RequestQueue queue = Volley.newRequestQueue(cxt);
                String url ="http://10.0.2.2:8080/user?user=" + login_user.toString() + "&password=" + login_pass.toString();  // login url
                HashMap<String, String> login_data = new HashMap();
                login_data.put("user",login_user.toString());
                login_data.put("password",login_pass.toString());
                JSONObject login_data_json = new JSONObject(login_data);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url,login_data_json ,
                                new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {


                                Log.d("Login ::","Response is: " + response.toString());
                                //textView.setText("Response: " + response.toString());

                                User u = gson.fromJson(response.toString(), User.class);

                                Log.d("LOGIN :: Success","" + u.getFullName());


                                // Apo8hkeush tou token sto Shared Preferences wste na fainetai pantou, ka8ws kai tou user Id pou 8a xreiastei se diafora posts
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("User_info", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("jwt_token",u.getPassword() ); // Storing string
                                editor.putInt("user_id",u.getId() );
                                editor.putString("user_full_name", u.getFullName());
                                editor.apply(); // commit changes

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // fortwnei to Main
                                startActivity(intent);

                                String welcome = "???????????????????? " + u.getFullName();// + response.fname + ;
                                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                                //Complete and destroy login activity once successful
                                finish();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("LOGIN :: Error","Error is: " + login_data_json.toString());

                                Toast.makeText(getApplicationContext(), "?? ?????????????? ??????????????, ?????????????????? ????????!", Toast.LENGTH_LONG).show();

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

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                Log.d("sign up", "success");
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}