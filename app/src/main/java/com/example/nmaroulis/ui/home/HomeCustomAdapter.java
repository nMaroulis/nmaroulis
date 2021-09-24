package com.example.nmaroulis.ui.home;


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
import com.example.nmaroulis.rest.Post;
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


public class HomeCustomAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Post> items; //data source of the list adapter
    private String jwt_token;
    private Integer uid;
    private int prof_icon = R.drawable.ic_friend_black_24dp;

    //public constructor
    public HomeCustomAdapter(Context context, ArrayList<Post> items) {
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
                    inflate(R.layout.home_list, parent, false);
        }

        // get current item to be displayed
        Post currentItem = (Post) getItem(position);

        ImageView post_flag = (ImageView)
                convertView.findViewById(R.id.post_flag);
        // get the TextView for item name and item description
        TextView post_profile = (TextView)
                convertView.findViewById(R.id.post_profile);
        TextView post_content = (TextView)
                convertView.findViewById(R.id.post_content);
        TextView post_date = (TextView)
                convertView.findViewById(R.id.post_date);

        ImageView post_like = (ImageView)
                convertView.findViewById(R.id.post_like);
        ImageView post_dislike = (ImageView)
                convertView.findViewById(R.id.post_dislike);
        TextView post_comments = (TextView)
                convertView.findViewById(R.id.post_comments);

        // Button button = (Button) convertView.findViewById(R.id.accept_con);
        //sets the text for item name and item description from the current item object
        post_profile.setText(currentItem.getTitle());
        post_content.setText(currentItem.getBody());
        post_date.setText(currentItem.getTime_posted());
        post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = currentItem.getId();
                Log.d("Home RES","ID is: "+ id );


                SharedPreferences pref = context.getSharedPreferences("User_info", 0);
                jwt_token = pref.getString("jwt_token", null); // fortwma tou jwt token
                uid = pref.getInt("user_id", -1); // fortwma tou user id


            }
        });
        // returns the view for the current row
        return convertView;
    }

    public void getTimeline(String jwt_token, Integer uid, Integer con_uid, Context cxt, String full_name){

        RequestQueue queue = Volley.newRequestQueue(cxt);

    }


}
