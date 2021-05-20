package com.example.melocommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.adapters.CommentsAdapter;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    TextView tvSongTitle;
    TextView tvArtist;
    ImageView ivSongPoster;
    String imageSongUrl;
    ImageView userImage;

    // item_comment
    private TextView tvUserName;
    private TextView tvComment;
    private ImageView ivUserImage;

    private RecyclerView rvComments;

    private CommentsAdapter commentsAdapter;
    private List<Comment> allComments;



    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvSongTitle = findViewById(R.id.tvUserName);
        tvArtist = findViewById(R.id.tvComment);
        ivSongPoster = findViewById(R.id.ivSongPoster);

        userImage = findViewById(R.id.userImage);

        Song song = Parcels.unwrap(getIntent().getParcelableExtra("Song"));

        Log.i(TAG, "Song: " + song.getName());

        tvSongTitle.setText(song.getName());
        tvArtist.setText(song.getArtist());
        imageSongUrl = song.getImageUrl();

        Glide.with(getApplicationContext())
                    .load(imageSongUrl)
                    .into(ivSongPoster);


        // Load User Image
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("SPOTIFY", 0);
        String userUrl = sharedPreferences.getString("imageUrl", "No User");

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(userUrl)
                .apply(options)
                .into(userImage);



        // item comment in rvComments
        tvUserName = findViewById(R.id.tvUserName);
        tvComment = findViewById(R.id.tvComment);
        ivUserImage = findViewById(R.id.ivUserImage);
        rvComments = findViewById(R.id.rvComments);

        allComments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(getApplicationContext(), allComments);

        // Steps to use the recycler view:
        // 0. create layout for one row in the list
        // 1. create the adapter
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvComments.setAdapter(commentsAdapter);
        // 4. set the layout manager on the recycler view
        rvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}