package com.example.nmaroulis.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.nmaroulis.MainActivity;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.FragmentProfileBinding;

import com.example.nmaroulis.rest.Post;
import com.example.nmaroulis.rest.User;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private Context cxt;
    private TextView profile_full_name, pTitle, pGender, pEducation, pWork, pPhone, pEmail, pResidense, pMember;
    private String jwt_token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        cxt = this.getContext();
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profile_full_name = binding.pFullName;
        pTitle = binding.pTitle;
        pGender = binding.pGender;
        pEducation = binding.pEducation;
        pWork = binding.pWork;
        pPhone = binding.pPhone;
        pEmail = binding.pEmail;
        pResidense = binding.pLocation;
        pMember = binding.pMember;

        SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
        jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token

        Integer uid = pref.getInt("user_id", -1); // fortwma tou user id
        String full_name = pref.getString("user_full_name", null);

        Gson gson = new Gson();

        RequestQueue queue = Volley.newRequestQueue(cxt);
        String url ="http://10.0.2.2:8080/users/"+uid.toString();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        User user = gson.fromJson(response, User.class);
                        Log.d("doInBackground :: User","Response is: "+ user.toString());

                        fillProfileForm(user);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RES","That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", jwt_token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        fillProfilePosts(uid,full_name, root);

//        String[] post_profile = { "Fabian sad", "Carlos dsad", "Alexd dasd asd", "Andrea dasdas", "Karla aaa",
//                "Freddy", "Lazaro"};
//        String[] post_content = { "Programmer", "Data Scientist", "Doctor", "Dddd", "Pez",
//                "Nicuro", "Bocachico" };

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fillProfilePosts(Integer uid, String full_name, View root){
        Gson gson = new Gson();

        RequestQueue queue = Volley.newRequestQueue(cxt);
        String url ="http://10.0.2.2:8080/users/"+uid.toString()+"/posts";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Post[] posts = gson.fromJson(response, Post[].class);

                        Log.d("fillProfilePosts","Response success ");

                        int prof_icon = R.drawable.ic_post_icon;

                        if(posts.length > 0 ) {
                            List<HashMap<String, String>> postList = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < posts.length; i++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("my_post_profile", full_name);
                                hm.put("my_post_content", posts[i].getTitle());
                                hm.put("my_post_flag", Integer.toString(prof_icon));
                                postList.add(hm);
                            }
                            String[] from = {"my_post_flag", "my_post_profile", "my_post_content"};
                            int[] to = {R.id.my_post_flag, R.id.my_post_profile, R.id.my_post_content};

                            ListView list = (ListView) root.findViewById(R.id.postlistView);
                            SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), postList, R.layout.list_my_posts, from, to);
                            list.setAdapter(adapter);

                        }else{
                            Log.d("fillProfilePosts","empty posts: ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RES fillProfilePosts ::","That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", jwt_token);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void fillProfileForm(User user){
        profile_full_name.setText(user.getFullName());
        pTitle.setText(user.getTitle().getName());
        pGender.setText(user.getGender());
        pEducation.setText(user.getEducation());
        pWork.setText(user.getWork());
        pPhone.setText(user.getPhone());
        pEmail.setText(user.getEmail());
        pResidense.setText(user.getResidense());
        pMember.setText(user.getMembersince());
    }

}


//    private class UserDetailsRequestTask extends AsyncTask<Integer, Void, User> {
//
//        @Override
//        protected User doInBackground(Integer... params) {
//            return
//        }
//
//        @Override
//        public void onPostExecute(User user) {
////
//        }
