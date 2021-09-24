package com.example.nmaroulis.ui.home;

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
import com.example.nmaroulis.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private String[] post_profile = { "Fabian sad", "Carlos dsad", "Alexd dasd asd", "Andrea dasdas", "Karla aaa",
            "Freddy", "Lazaro"};
    private String[] post_content = { "Programmer", "Data Scientist", "Doctor", "Dddd", "Pez",
            "Nicuro", "Bocachico" };
    int prof_icon = R.drawable.ic_post_icon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        if (container != null) {
//            container.removeAllViews();
//        }

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        List<HashMap<String,String>> postList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<7;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("post_profile", post_profile[i]);
            hm.put("post_content","Title : " + post_content[i]);
            hm.put("post_flag", Integer.toString(prof_icon) );
            postList.add(hm);
        }
        String[] from = { "post_flag","post_profile","post_content" };
        int[] to = { R.id.post_flag,R.id.post_profile,R.id.post_content};

        ListView list = (ListView)root.findViewById(R.id.homelistView);
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), postList, R.layout.home_list, from, to);
        list.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}