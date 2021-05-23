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
    private TextView songView;
    private Button addBtn;
    private Button currentBtn;
    private Song song;
    private ImageView imageProfile;
    private ImageView imageSong;
    private Context context;
    private String imageSongUrl;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;

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

        songService = new SongService(getContext().getApplicationContext());
        userView = view.findViewById(R.id.user);
        //addBtn = view.findViewById(R.id.add);
        imageProfile = view.findViewById(R.id.imageProfile);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("display_name", "No User"));
        String userUrl = sharedPreferences.getString("imageUrl", "No User");

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(userUrl)
                .apply(options)
                .into(imageProfile);

        //Log.i("MainActivity", sharedPreferences.getAll().toString());

        //getTracks();

        //addBtn.setOnClickListener(addListener);

    }

    private final View.OnClickListener addListener = v -> {
        songService.addSongToLibrary(this.song);
        //Toast.makeText(getActivity(),"\"" + this.song.getName() + "\" was added to your library!",Toast.LENGTH_SHORT).show();
    };


    private void getTracks() {
//            songService.getCurrentlyPlaying(() -> {
//                song = songService.getSong();
//                updateSong();
//            });

        songService.getRecentlyPlayedTracks(()->{
            recentlyPlayedTracks = songService.getSongs();
            updateSongRecent();
        });

    }

    private void updateSongRecent() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText("Last song played: " + recentlyPlayedTracks.get(0).getName() +" by " + recentlyPlayedTracks.get(0).getArtist());
        }
        song = recentlyPlayedTracks.get(0);

        imageSongUrl = song.getImageUrl();

        Glide.with(this)
                .load(imageSongUrl)
                .into(imageSong);
    }

    private void updateSong() {
       songView.setText("Playing: " + song.getName() + " by " + song.getArtist());
    }



}