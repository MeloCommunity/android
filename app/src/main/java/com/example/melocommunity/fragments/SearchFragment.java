package com.example.melocommunity.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melocommunity.Connectors.SongService;
import com.example.melocommunity.R;
import com.example.melocommunity.adapters.SearchSongsAdapter;
import com.example.melocommunity.models.Song;
import java.util.ArrayList;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    private TextView tvSongTitle;
    private TextView tvArtist;
    private AppCompatEditText etSongName;
    private Button btnSearch;

    private Song song;
    private ImageView ivSongPoster;

    private Context context;
    private String imageSongUrl;

    private SongService songService;
    private ArrayList<Song> searchTracks;

    private RecyclerView rvPosts;
    private SearchSongsAdapter searchSongsAdapter;
    private List<Song> allFeedSongs;

    private String songName;


    public SearchFragment() {
        // Required empty public constructor
    }


    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
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
        etSongName = view.findViewById(R.id.etSongName);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvPosts = view.findViewById(R.id.rvPosts);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTracks();
            }
        });


        allFeedSongs = new ArrayList<>();
        searchSongsAdapter = new SearchSongsAdapter(getContext(), allFeedSongs);

        // Steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvPosts.setAdapter(searchSongsAdapter);
        // 4. set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        //queryPosts() not implemented yet


    }

    private void getTracks() {

        if (!allFeedSongs.isEmpty()){
            searchTracks.clear();
            allFeedSongs.clear();
        }

        songService.getSearchTracks(() -> {
            searchTracks = songService.getSongs();
            allFeedSongs.addAll(searchTracks);
            searchSongsAdapter.notifyDataSetChanged();
            Log.i(TAG, "SEARCH: " + allFeedSongs );

        }, etSongName.getText().toString());
    }
}
