package com.example.nmaroulis.ui.connections;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nmaroulis.R;
import com.example.nmaroulis.rest.User;
// import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;

public class connectionsTagAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> connections;
    private List<User> connectionsCopy;
    private HashMap<User, Boolean> pressed;
    private List<Boolean> selected;
    private List<Boolean> visible;

    // Constructor
    public connectionsTagAdapter(Context c, List<User> connections, HashMap<User, Boolean> pressed) {
        Logger.getAnonymousLogger().warning("Connection Added");
        mContext = c;
        this.connections = connections;
        this.pressed = pressed;
        selected = new ArrayList<>();
        visible = new ArrayList<>();
        for (int i = 0; i < connections.size(); i++){
            selected.add(false);
            visible.add(true);
        }
        connectionsCopy = new ArrayList<>();
        connectionsCopy.addAll(connections);
//        createRecords();
    }

    public int getCount() {
        assert connections != null;
        return connections.size();
    }

    public void filter(String text) {
        connections.clear();
        if(text.isEmpty()){
            connections.addAll(connectionsCopy);
        } else{
            text = text.toLowerCase();
            for(User item: connectionsCopy){
                if(item.getFname().toLowerCase().contains(text) || item.getLname().toLowerCase().contains(text)){
                    connections.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        CircleImageView imageView;
        LinearLayout linearLayout;
        TextView tv;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        if (convertView == null) {
//            linearLayout = (LinearLayout) inflater.inflate(R.layout.connections_list, null);
//        } else {
//            linearLayout = (LinearLayout) convertView;
//        }
//        imageView = linearLayout.findViewById(R.id.tag_pic);
//        Picasso.get().load(students.get(position).getImage())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(imageView);
//
//
//        tv = linearLayout.findViewById(R.id.tag_name);
//
//        tv.setText(students.get(position).getName());
//        if (pressed.get(connections.get(position))) {
//            linearLayout.setBackgroundResource(R.drawable.tag_selector_shape);
//
//        } else {
//            linearLayout.setBackgroundColor(Color.WHITE);
//        }

//        imageView.setImageResource(students.get(position));
        return null;
    }

    public void setSelected(boolean b) {

        if (b) {
            for (int i = 0; i < getCount(); i++) {
                pressed.put(connections.get(i), true);
            }


        } else {
            for (int i = 0; i < getCount(); i++) {
                pressed.put(connections.get(i), false);
            }

        }
        notifyDataSetChanged();

    }
}