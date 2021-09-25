package com.example.nmaroulis.ui.connections;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.FragmentConnectionsBinding;
import com.example.nmaroulis.rest.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionsFragment extends Fragment {

    private ConnectionsViewModel connectionsViewModel;
    private FragmentConnectionsBinding binding;
    private Context cxt;
    private String jwt_token;
    private Integer uid;
    private User[] connections;
    private User[] pending_connections;
    private User[] connection_requests;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> user_ids=new ArrayList<>();
    SpinnerDialog spinnerDialog;
    private Button showConnectionsButton;
    private SearchView searchView;


    int prof_icon = R.drawable.ic_friend_black_24dp;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        connectionsViewModel = new ViewModelProvider(this).get(ConnectionsViewModel.class);
        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        cxt = this.getContext();
        final TextView textView = binding.textConnections;
        connectionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        showConnectionsButton = binding.showConnectionsButton;



//        items.add("Mumbai");
//        items.add("Delhi");
//        items.add("Bengaluru");
//        items.add("Hyderabad");
//        items.add("Ahmedabad");
//        items.add("Chennai");
//        items.add("Kolkata");
//        items.add("Surat");
//        items.add("Pune");
//        items.add("Jaipur");
//        items.add("Lucknow");
//        items.add("Kanpur");

        SharedPreferences pref = cxt.getSharedPreferences("User_info", 0);
        jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
        uid = pref.getInt("user_id", -1); // fortwma tou user id

        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user_search/" + uid.toString();  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        User[] con_search = gson.fromJson(response, User[].class);
                        Log.d("Conn Search :: User","Response is: "+ response);

                        for(int i=0;i<con_search.length;i++){
                            items.add(con_search[i].getFullName());
                            user_ids.add(Integer.toString(con_search[i].getId()));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Conn Search RES","That didn't work!");
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

        spinnerDialog = new SpinnerDialog(getActivity(), items,
                "Αναζήτηση Χρηστών", user_ids);

        spinnerDialog.setTitleColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchIconColor(getResources().getColor(R.color.black));
        spinnerDialog.setSearchTextColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemColor(getResources().getColor(R.color.black));
        spinnerDialog.setItemDividerColor(getResources().getColor(R.color.purple_700));
        spinnerDialog.setCloseColor(getResources().getColor(R.color.black));

        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position, String connection_id) {

                Toast.makeText(getActivity(), item + "  " + position + "", Toast.LENGTH_SHORT).show();
                Log.d("search :: User","Response is: "+ item + " " + position + " " + connection_id);

                sendConnectionRequest(jwt_token, uid, connection_id, root);
            }
        });

        showConnectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });



        getProfileConnections(jwt_token, uid, root);
        getProfileRequests(jwt_token, uid, root);
        getProfilePending(jwt_token, uid, root);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getProfileConnections(String jwt_token, Integer uid, View root){

        /*  GET CONNECTIONS */
        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/connections";  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        connections = gson.fromJson(response, User[].class);
                        Log.d("connections :: User","Response is: "+ response.toString());

                        List<HashMap<String,String>> friendsList = new ArrayList<HashMap<String,String>>();
                        for(int i=0;i<connections.length;i++){
                            HashMap<String, String> hm = new HashMap<String,String>();
                            hm.put("txt", connections[i].getFullName());
                            hm.put("cur","Επαγγελματική θέση: " + connections[i].getTitle().getName());
                            hm.put("con_work","Φορέας Απασχόλησης: " + connections[i].getWork());
                            hm.put("flag", Integer.toString(prof_icon) );
                            friendsList.add(hm);
                        }
                        String[] from = { "flag","txt","cur","con_work" };
                        int[] to = { R.id.flag,R.id.txt,R.id.cur, R.id.con_work};

                        ListView list = (ListView)root.findViewById(R.id.connectionslistView);
                        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), friendsList, R.layout.connections_list, from, to);
                        list.setAdapter(adapter);
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
        queue.add(stringRequest);
        /* GET CONNECTIONS END */
    }

    public void getProfilePending(String jwt_token, Integer uid, View root){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/responses";  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pending_connections = gson.fromJson(response, User[].class);
                        Log.d("Pending :: User","Response is: "+ response.toString());

                        ArrayList<User> pending_connections_list =  new ArrayList<>(Arrays.asList(pending_connections));
                        ConnectionsCustomAdapter connectionsCustomAdapter = new ConnectionsCustomAdapter(cxt,pending_connections_list);
                        ListView list = (ListView)root.findViewById(R.id.pendinglistView);
                        list.setAdapter(connectionsCustomAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Pending RES","That didn't work!");
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

    public void getProfileRequests(String jwt_token, Integer uid, View root){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/requests";  // new post url

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        connection_requests= gson.fromJson(response, User[].class);
                        Log.d("request :: User","Response is: "+ response.toString());

                        List<HashMap<String,String>> friendsList = new ArrayList<HashMap<String,String>>();
                        for(int i=0;i<connection_requests.length;i++){
                            HashMap<String, String> hm = new HashMap<String,String>();
                            hm.put("clr_txt", connection_requests[i].getFullName());
                            hm.put("clr_cur","Επαγγελματική θέση: " + connection_requests[i].getTitle().getName());
                            hm.put("clr_con_work","Φορέας Απασχόλησης: " + connection_requests[i].getWork());
                            hm.put("clr_flag", Integer.toString(prof_icon) );
                            friendsList.add(hm);
                        }
                        String[] from = { "clr_flag","clr_txt","clr_cur","clr_con_work" };
                        int[] to = { R.id.clr_flag,R.id.clr_txt,R.id.clr_cur, R.id.clr_con_work};

                        ListView list = (ListView)root.findViewById(R.id.requestslistView);
                        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), friendsList, R.layout.connections_list_request, from, to);
                        list.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("request RES","That didn't work!");
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


    public void sendConnectionRequest(String jwt_token, Integer uid, String connection_id, View root){

        RequestQueue queue = Volley.newRequestQueue(cxt);
        Gson gson = new Gson();
        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/request/" + connection_id;  // new request url

        HashMap<String, String> newrequest_data = new HashMap();
        JSONObject newpost_data_json = new JSONObject(newrequest_data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,newpost_data_json ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("New Request ::","Response is: " + response.toString());

                                String welcome = "Το αίτημα σας στάλθηκε επιτυχώς";
                                Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();

                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);                                    }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("NewRequest :: Error","Error is: " + newpost_data_json.toString());

                        Toast.makeText(cxt, "Το αίτημα σας δεν στάλθηκε", Toast.LENGTH_LONG).show();

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