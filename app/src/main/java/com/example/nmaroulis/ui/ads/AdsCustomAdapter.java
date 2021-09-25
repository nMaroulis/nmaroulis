package com.example.nmaroulis.ui.ads;

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
import com.example.nmaroulis.rest.Ad;
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


public class AdsCustomAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Ad> items; //data source of the list adapter
    private String jwt_token;
    private Integer uid;
    private int prof_icon = R.drawable.ic_friend_black_24dp;

    //public constructor
    public AdsCustomAdapter(Context context, ArrayList<Ad> items) {
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
                    inflate(R.layout.ads_list, parent, false);
        }

        Ad currentItem = (Ad) getItem(position);

        //        ImageView clp_flag = (ImageView)
        //                convertView.findViewById(R.id.clp_flag);

        // get the TextView for item name and item description
        TextView ad_created_by = (TextView)
                convertView.findViewById(R.id.ad_created_by);
        TextView ad_title = (TextView)
                convertView.findViewById(R.id.ad_title);
        TextView ad_position = (TextView)
                convertView.findViewById(R.id.ad_position);
        TextView ad_body = (TextView)
                convertView.findViewById(R.id.ad_body);
        TextView ad_date = (TextView)
                convertView.findViewById(R.id.ad_date);
        Button button = (Button) convertView.findViewById(R.id.ad_apply);
        //sets the text for item name and item description from the current item object
        ad_created_by.setText(currentItem.getCreated_by());
        ad_title.setText(currentItem.getTitle());
        ad_position.setText(currentItem.getPosition());
        ad_body.setText(currentItem.getBody());
        ad_date.setText(currentItem.getTime_posted());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = currentItem.getId();
                Log.d("Ad RES","ID is: "+ id );


                SharedPreferences pref = context.getSharedPreferences("User_info", 0);
                jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
                uid = pref.getInt("user_id", -1); // fortwma tou user id

                //applyToAd(jwt_token,uid ,id, context, currentItem.getId(), currentItem.getCreated_by());
                String welcome = "Η αίτηση στην αγγελία του χρήστη " + currentItem.getCreated_by() + " ήταν επιτυχής";


            }
        });
        return convertView;
    }

    public void applyToAd(String jwt_token, Integer uid, Integer con_uid, Context cxt, Integer ad_id, String full_name){

        RequestQueue queue = Volley.newRequestQueue(cxt);

        String url ="http://10.0.2.2:8080/user/" + uid.toString() + "/ads/" + ad_id + "/apply";

        HashMap<String, String> accept_con_data = new HashMap();
        JSONObject accept_con_data_json = new JSONObject(accept_con_data);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url,accept_con_data_json ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.d("Accept Con ::","Response is: " + response.toString());

                                String welcome = "Η αίτηση στην αγγελία του χρήστη " + full_name + " ήταν επιτυχής";
                                Toast.makeText(cxt, welcome, Toast.LENGTH_LONG).show();
                                //finish();  //Complete and destroy activity once successful

                                // notifyConnectionRequest(jwt_token, uid,con_uid, cxt);
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




}
