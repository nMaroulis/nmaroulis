package com.example.nmaroulis.ui.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.databinding.FragmentPostBinding;
import com.example.nmaroulis.ui.login.LoginActivity;
import com.example.nmaroulis.ui.sign_up.SignUpActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

public class PostFragment extends Fragment {

    private PostViewModel postViewModel;
    private FragmentPostBinding binding;
    private Context cxt;
    private TextInputLayout post_title, post_body, post_image, post_music, post_public;
    private Button new_post, my_posts;
    private Switch public_switch;
    private Integer uid;
    private String jwt_token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        postViewModel =
                new ViewModelProvider(this).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        post_title = binding.newPostTitle;
        post_body = binding.newPostBody;
        new_post = binding.postButton;
        public_switch = binding.postPublicSwitch;

        cxt = this.getContext();
        final TextView textView = binding.textPost;
        postViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
        jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token

        uid = pref.getInt("user_id", -1); // fortwma tou user id



        new_post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                RequestQueue queue = Volley.newRequestQueue(cxt);
                String g;

                if(public_switch.isChecked()){
                    g = "true";
                }else{
                    g = "false";
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time_posted = dtf.format(now); // pros8etei to pote egine to signup

                SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
                String full_name = pref.getString("user_full_name", null); // fortwma tou jwt token


                String url ="http://10.0.2.2:8080/users/" + uid.toString() + "/posts?title=" + full_name+//post_title.getEditText().getText().toString() +
                        "&body=" + post_title.getEditText().getText().toString() + " " + post_body.getEditText().getText().toString() +
                        "&accesibility=" + g + "&time_posted=" + time_posted;  // new post url

                HashMap<String, String> newpost_data = new HashMap();
                newpost_data.put("title",post_title.getEditText().getText().toString());
                newpost_data.put("body",post_body.getEditText().getText().toString());
                newpost_data.put("time_posted",time_posted);
                newpost_data.put("accesibility",g);
                JSONObject newpost_data_json = new JSONObject(newpost_data);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url,newpost_data_json ,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.d("New Post ::","Response is: " + response.toString());

                                        // Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // fortwnei to Main
                                        // startActivity(intent);

                                        String welcome = "Η Ανάρτηση σας ήταν Επιτυχής";
                                        Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();
                                        //finish();  //Complete and destroy activity once successful

                                        Intent intent = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intent);                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.d("NewPost :: Error","Error is: " + newpost_data_json.toString());

                                Toast.makeText(cxt, "Η Ανάρτηση σας απέτυχε", Toast.LENGTH_LONG).show();

                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", jwt_token);
                        params.put("Content-Type", "application/x-www-form-urlencoded");//"application/json");
                        return params;
                    }
                };
                queue.add(jsonObjectRequest);   // Add the request to the RequestQueue.

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}