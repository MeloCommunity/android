package com.example.melocommunity.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melocommunity.Connectors.SongService;
import com.example.melocommunity.R;
import com.example.melocommunity.adapters.CommentsAdapter;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;
import java.util.ArrayList;
import com.example.melocommunity.adapters.FeedSongsAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public static final String TAG = "FeedFragment";

    private TextView tvSongTitle;
    private TextView tvArtist;

    private Song song;
    private ImageView ivSongPoster;

    private Context context;
    private String imageSongUrl;

    private SongService songService;
    private ArrayList<Song> topTracks;

    private RecyclerView rvPosts;
    private FeedSongsAdapter feedSongsAdapter;
    private List<Song> allFeedSongs;

    private RecyclerView rvComments;

    private CommentsAdapter commentsAdapter;
    private List<Comment> allComments;

    private ImageView userImage3;
    private Button btnPost3;
    private EditText etDescription;
    private String userName;



    public FeedFragment() {
        // Required empty public constructor
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        songService = new SongService(getContext().getApplicationContext());
        tvSongTitle = view.findViewById(R.id.tvUserName);
        tvArtist = view.findViewById(R.id.tvComment);
        ivSongPoster = view.findViewById(R.id.ivSongPoster);
        rvPosts = view.findViewById(R.id.rvPosts);
        btnPost3 = view.findViewById(R.id.btnPost3);
        userImage3 = view.findViewById(R.id.userImage3);
        etDescription = view.findViewById(R.id.etEditComment);

        getTracks();

        allFeedSongs = new ArrayList<>();
        feedSongsAdapter = new FeedSongsAdapter(getContext(), allFeedSongs);

        // Steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvPosts.setAdapter(feedSongsAdapter);
        // 4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getTracks() {

        songService.getTopTracks(() -> {
            topTracks = songService.getSongs();
            allFeedSongs.addAll(topTracks);
            feedSongsAdapter.notifyDataSetChanged();
            Log.i(TAG, "FEED: " + allFeedSongs );

        });
    }
}
