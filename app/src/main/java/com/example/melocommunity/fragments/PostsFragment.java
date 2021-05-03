package com.example.melocommunity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melocommunity.LikedSongsAdapter;
import com.example.melocommunity.R;
import com.example.melocommunity.models.LikedSongs;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    private RecyclerView rvPosts;
    private LikedSongsAdapter adapter;
    private List<LikedSongs> allLikedSongs;

    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        allLikedSongs = new ArrayList<>();
        adapter = new LikedSongsAdapter(getContext(), allLikedSongs);

        // Steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        //queryPosts() not implemented yet
    }
}