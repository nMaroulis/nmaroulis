package com.example.nmaroulis.ui.post;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nmaroulis.ConnectionHelper;
import com.example.nmaroulis.databinding.FragmentPostBinding;

import java.sql.Connection;

public class PostFragment extends Fragment {

    private PostViewModel postViewModel;
    private FragmentPostBinding binding;
    Connection connect;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postViewModel =
                new ViewModelProvider(this).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPost;
        postViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            //connect = connectionHelper.connectionclass();

            if(connect != null){
                //                String query = "Select * from profile;";
                //                Statement st = connect.createStatement();
                //                ResultSet rs = st.executeQuery(query);
                //while(rs.next()){
                //    String aa = rs.getString(1);
                //    Log.e("Value", aa);
                // mText.setValue(rs.getString(1));
                //}
                Log.e("Value", connect.toString());
            }
            else {
                Log.e("Value", "conn null");
            }
        } catch(Exception ex) {
            Log.e("Error", ex.getMessage());
        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}