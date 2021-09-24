package com.example.nmaroulis.ui.connections;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.example.nmaroulis.R;
import com.example.nmaroulis.rest.User;
import com.google.gson.Gson;
// import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;


public class ConnectionsCustomAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<User> items; //data source of the list adapter
    private String jwt_token;
    private Integer uid;
    private int prof_icon = R.drawable.ic_friend_black_24dp;

    //public constructor
    public ConnectionsCustomAdapter(Context context, ArrayList<User> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.connections_list_pending, parent, false);
        }

        // get current item to be displayed
        User currentItem = (User) getItem(position);

        ImageView clp_flag = (ImageView)
                convertView.findViewById(R.id.clp_flag);
        // get the TextView for item name and item description
        TextView clp_txt = (TextView)
                convertView.findViewById(R.id.clp_txt);
        TextView clp_cur = (TextView)
                convertView.findViewById(R.id.clp_cur);
        TextView clp_con_work = (TextView)
                convertView.findViewById(R.id.clp_con_work);
        Button button = (Button) convertView.findViewById(R.id.accept_con);
        //sets the text for item name and item description from the current item object
        clp_txt.setText(currentItem.getFullName());
        clp_cur.setText(currentItem.getTitle().getName());
        clp_con_work.setText(currentItem.getWork());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = currentItem.getId();
                Log.d("Pending RES","ID is: "+ id );


                SharedPreferences pref = context.getSharedPreferences("User_info", 0);
                jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
                uid = pref.getInt("user_id", -1); // fortwma tou user id

                acceptRequest(jwt_token,uid ,id, context, currentItem.getFullName());


            }
        });
        // returns the view for the current row
        return convertView;
    }

    public void acceptRequest(String jwt_token, Integer uid, Integer con_uid, Context cxt, String full_name){

        RequestQueue queue = Volley.newRequestQueue(cxt);

        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/response/" + con_uid.toString();

        HashMap<String, String> accept_con_data = new HashMap();
        JSONObject accept_con_data_json = new JSONObject(accept_con_data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,accept_con_data_json ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Accept Con ::","Response is: " + response.toString());

                                String welcome = "Η Σύνδεση με τον Χρήστη " + full_name + " ήταν επιτυχής";
                                Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();
                                //finish();  //Complete and destroy activity once successful

                                notifyConnectionRequest(jwt_token, uid,con_uid, cxt);
                                 }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("Accept Con :: Error","Error is: " + accept_con_data_json.toString());

                        Toast.makeText(cxt, "Η Αποδοχή σας απέτυχε", Toast.LENGTH_LONG).show();

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
        queue.add(jsonObjectRequest);

    }


    public void notifyConnectionRequest(String jwt_token, Integer uid, Integer con_uid, Context cxt){

        RequestQueue queue = Volley.newRequestQueue(cxt);

        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/response/" + con_uid.toString() + "/notify_connection";

        HashMap<String, String> accept_con_data = new HashMap();
        JSONObject accept_con_data_json = new JSONObject(accept_con_data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,accept_con_data_json ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Notify Con ::","Response is: " + response.toString());

                                //finish();  //Complete and destroy activity once successful

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("Notify Con :: Error","Error is: " + accept_con_data_json.toString());

                        Toast.makeText(cxt, "Η Αποδοχή σας απέτυχε", Toast.LENGTH_LONG).show();

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
        queue.add(jsonObjectRequest);

    }




}
