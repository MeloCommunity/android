package com.example.melocommunity.fragments;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.Connectors.SongService;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Song;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView userView;
    private TextView email;
    private TextView country;

    private ImageView imageProfile;


    public AccountFragment() {
        // Required empty public constructor
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userView = view.findViewById(R.id.user);
        imageProfile = view.findViewById(R.id.imageProfile);
        email = view.findViewById(R.id.email);
        country = view.findViewById(R.id.country);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);
        userView.setText("Welcome, " + sharedPreferences.getString("display_name", "No User")+ "!");
        String userUrl = sharedPreferences.getString("imageUrl", "No User");
        email.setText("Email:" + "\n" + sharedPreferences.getString("email", "No User"));
        country.setText("Country:" + "\n" + sharedPreferences.getString("country", "No User"));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(userUrl)
                .apply(options)
                .into(imageProfile);


    }

}