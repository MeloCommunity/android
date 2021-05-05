package com.example.melocommunity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.melocommunity.Connectors.SongService;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Song;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private TextView tvSongTitle;
    private TextView tvArtist;

    private Song song;
    private ImageView ivSongPoster;

    private Context context;
    private String imageSongUrl;

    private SongService songService;
    private ArrayList<Song> topTracks;

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
        tvSongTitle = view.findViewById(R.id.tvSongTitle);
        tvArtist = view.findViewById(R.id.tvArtist);
        ivSongPoster = view.findViewById(R.id.ivSongPoster);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);

        getTracks();

    }

    private void getTracks() {

        songService.getTopTracks(()->{
            topTracks = songService.getSongs();
            updateTopSongs();
    });
}

    private void updateTopSongs() {
        if (topTracks.size() > 0) {
            tvSongTitle.setText(topTracks.get(5).getName());
            tvArtist.setText( topTracks.get(5).getArtist());
        }
        song = topTracks.get(5);

        imageSongUrl = song.getImageUrl();

        Glide.with(this)
                .load(imageSongUrl)
                .into(ivSongPoster);
    }
    }