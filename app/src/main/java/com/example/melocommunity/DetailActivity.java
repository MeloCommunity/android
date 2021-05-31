package com.example.melocommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.adapters.CommentsAdapter;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    TextView tvSongTitle;
    TextView tvArtist;
    TextView lengthSong;
    ImageView ivSongPoster;
    String imageSongUrl;
    ImageView userImage;
    Button btnPost;
    EditText etDescription;
    private ImageView btnDelete;
    String userName;
    String userID;


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
        btnPost = findViewById(R.id.btnPost);
        etDescription = findViewById(R.id.tiComment);
        lengthSong = findViewById(R.id.lengthSong);
        btnDelete = findViewById(R.id.btnDelete);




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
        userID = (sharedPreferences.getString("userid", "No User"));

        Integer minutes = (song.getRelease()/1000/60);
        String min = minutes.toString();
        if (min.length()==1) min = '0'+min;
        Integer seconds = (song.getRelease()/1000)%60;
        String sec = seconds.toString();
        if (sec.length()==1) sec = '0'+sec;
        lengthSong.setText(min+":"+sec);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(userUrl)
                .apply(options)
                .into(userImage);


        // item comment in rvComments

        userName = (sharedPreferences.getString("display_name", "No User"));

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
        queryComments(song.getId());


        //On click that sends the information to postComment
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("CHECKING", userName + " " + userUrl);
                postComment(description, userName, userUrl, song.getId(), userID);
            }
        });



    }
    protected void queryComments(String id) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo("songID", id);
        Log.d(TAG, "song id is " + id);
        query.include(Comment.KEY_SONGID);
        query.setLimit(20);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Comment comment : comments) {

                    Log.i(TAG, "Comment: " + comment.getDescription() + ", username: " + comment.getUserName());
                }
                allComments.addAll(comments);
                commentsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void postComment(String description, String userName, String userImageUrl, String songID, String userID) {
        Comment comment = new Comment();
        comment.setSongID(songID);
        comment.setUserID(userID);
        comment.setDescription(description);
        comment.setUserName(userName);
        comment.setUserImageUrl(userImageUrl);
        comment.saveInBackground(e -> {
            if (e==null){
                allComments.add(comment);
                commentsAdapter.notifyDataSetChanged();
                etDescription.setText("");
                //Save was done
            }else{
                //Something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}