package com.example.nmaroulis.ui.ads;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.FragmentAdsBinding;
import com.example.nmaroulis.rest.Ad;
import com.example.nmaroulis.rest.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdsFragment extends Fragment {

    private AdsViewModel adsViewModel;
    private FragmentAdsBinding binding;
    private Context cxt;
    private String jwt_token, full_name;
    private Integer uid;
    private Ad[] ads;
    Button postAd;
    private TextInputLayout new_ad_title, new_ad_body, new_ad_position;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       adsViewModel =
                new ViewModelProvider(this).get(AdsViewModel.class);

        binding = FragmentAdsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cxt = this.getContext();
        postAd = binding.createAdButton;
        new_ad_title = binding.newAdTitle;
        new_ad_body = binding.newAdBody;
        new_ad_position = binding.newAdPosition;
        final TextView textView = binding.textAds;
        adsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
        jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
        uid = pref.getInt("user_id", -1); // fortwma tou user id
        full_name = pref.getString("user_full_name", null);
        getAds(jwt_token, uid, root);


        postAd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(new_ad_title == null || new_ad_body == null || new_ad_position == null){
                    String welcome = "Συμπληρώστε όλα τα πεδία";
                    Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();

                }
                createAd(jwt_token, uid,full_name, root);
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getAds(String jwt_token, Integer uid, View root){

        /*  GET CONNECTIONS */
        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/users/" + uid.toString() + "/ads";  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ads = gson.fromJson(response, Ad[].class);
                        Log.d("ads :: Ad","Response is: "+ response.toString());

                        ArrayList<Ad> ads_list =  new ArrayList<>(Arrays.asList(ads));
                        AdsCustomAdapter adsCustomAdapter = new AdsCustomAdapter(cxt,ads_list);
                        ListView list = (ListView)root.findViewById(R.id.adslistView);
                        list.setAdapter(adsCustomAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Ads RES","That didn't work!");
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
        /* GET CONNECTIONS END */
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createAd(String jwt_token, Integer uid, String full_name, View root){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        String g;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time_posted = dtf.format(now); // pros8etei to pote egine to signup


        String url ="http://10.0.2.2:8080/users/" + uid.toString() + "/ads?title=" + new_ad_title.getEditText().getText().toString() +
                "&body=" + new_ad_body.getEditText().getText().toString() + "&position=" + new_ad_position.getEditText().getText().toString() +
                "&created_by=" + full_name + "&time_posted=" + time_posted;  // new post url

        HashMap<String, String> newad_data = new HashMap();
        newad_data.put("title", new_ad_title.getEditText().getText().toString());
        newad_data.put("body",new_ad_body.getEditText().getText().toString());
        newad_data.put("position",new_ad_position.getEditText().getText().toString());
        newad_data.put("time_posted",time_posted);
        newad_data.put("created_by",full_name);
        JSONObject newad_data_json = new JSONObject(newad_data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,newad_data_json ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("New Ad ::","Response is: " + response.toString());

                                // Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // fortwnei to Main
                                // startActivity(intent);

                                String welcome = "Η Δημιουργία της Αγγελίας σας ήταν Επιτυχής";
                                Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();
                                //finish();  //Complete and destroy activity once successful

                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);                                    }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("NewAd :: Error","Error is: " + newad_data_json.toString());

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

}