package com.example.nmaroulis.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.FragmentHomeBinding;
import com.example.nmaroulis.rest.Post;
import com.example.nmaroulis.rest.User;
import com.example.nmaroulis.ui.connections.ConnectionsCustomAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Context cxt;
    private String jwt_token;
    private Integer uid;
    private Post[] timeline_posts;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cxt = this.getContext();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
        jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
        uid = pref.getInt("user_id", -1); // fortwma tou user id

        getUserTimeline(jwt_token, uid, root);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getUserTimeline(String jwt_token, Integer uid, View root){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/timeline";  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        timeline_posts = gson.fromJson(response, Post[].class);
                        Log.d("Timeline RES","Response is: "+ response.toString());

                        if(timeline_posts == null){

                            TextView emptyText = (TextView)root.findViewById(R.id.empty_results);
                            emptyText.setVisibility(View.VISIBLE);
                            ListView list = (ListView)root.findViewById(R.id.homelistView);
                            list.setEmptyView(emptyText);
                        }
                        else if (timeline_posts.length == 0){
                            TextView emptyText = (TextView)root.findViewById(R.id.empty_results);
                            emptyText.setVisibility(View.VISIBLE);
                            ListView list = (ListView)root.findViewById(R.id.homelistView);
                            list.setEmptyView(emptyText);
                        }
                        else {
                            ArrayList<Post> home_list = new ArrayList<>(Arrays.asList(timeline_posts));
                            HomeCustomAdapter homeCustomAdapter = new HomeCustomAdapter(cxt, home_list);
                            ListView list = (ListView) root.findViewById(R.id.homelistView);
                            list.setAdapter(homeCustomAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Timeline RES","That didn't work!");

                TextView emptyText = (TextView)root.findViewById(R.id.empty_results);
                emptyText.setVisibility(View.VISIBLE);
                ListView list = (ListView)root.findViewById(R.id.homelistView);
                list.setEmptyView(emptyText);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", jwt_token);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}