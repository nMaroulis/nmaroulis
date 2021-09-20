package com.example.nmaroulis.ui.connections;

import android.os.Bundle;
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

import com.example.nmaroulis.R;
import com.example.nmaroulis.databinding.FragmentConnectionsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionsFragment extends Fragment {

    private ConnectionsViewModel connectionsViewModel;
    private FragmentConnectionsBinding binding;
    private String[] full_names = { "Fabian sad", "Carlos dsad", "Alexd dasd asd", "Andrea dasdas", "Karla aaa",
            "Freddy", "Lazaro", "Hector", "Carolina", "Edwin", "Jhon",
            "Edelmira", "Andres","Fabian", "Carlos", "Alex", "Andrea", "Karla",
            "Freddy", "Lazaro", "Hector", "Carolina", "Edwin", "Jhon",
            "Edelmira", "Andres" };

    private String[] titles = { "Programmer", "Data Scientist", "Doctor", "Dddd", "Pez",
            "Nicuro", "Bocachico", "Chucha", "Curie", "Raton", "Aguila",
            "Leon", "Jirafa","Perro", "Gato", "Oveja", "Elefante", "Pez",
            "Nicuro", "Bocachico", "Chucha", "Curie", "Raton", "Aguila",
            "Leon", "Jirafa" };

    int prof_icon = R.drawable.ic_friend_black_24dp;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        connectionsViewModel = new ViewModelProvider(this).get(ConnectionsViewModel.class);

        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textConnections;
        connectionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        List<HashMap<String,String>> friendsList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<20;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", full_names[i]);
            hm.put("cur","Title : " + titles[i]);
            hm.put("flag", Integer.toString(prof_icon) );
            friendsList.add(hm);
        }
        String[] from = { "flag","txt","cur" };
        int[] to = { R.id.flag,R.id.txt,R.id.cur};

        //View v = inflater.inflate(R.layout.fragment_connections, container,false);
        ListView list = (ListView)root.findViewById(R.id.connectionslistView);
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), friendsList, R.layout.connections_list, from, to);
        list.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}