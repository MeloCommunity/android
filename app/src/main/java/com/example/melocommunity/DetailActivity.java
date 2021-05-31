package com.example.melocommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.Connectors.SongService;
import com.example.melocommunity.Connectors.UserService;
import com.example.melocommunity.adapters.CommentsAdapter;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;
import com.example.melocommunity.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.parse.ParseUser;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

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
    Button btnPost;
    ImageView btnPlay;
    EditText etDescription;
    private ImageView btnDelete;
    String userName;
    String userID;

    private RecyclerView rvComments;

    private CommentsAdapter commentsAdapter;
    private List<Comment> allComments;

    private Context context;

    private static final String CLIENT_ID =  "676d4db0d44b4f95956d8efa0ff25ff8";
    private static final String REDIRECT_URI = "com.example.melocommunity://callback";

    protected SpotifyAppRemote mSpotifyAppRemote;
    private int stateDrawPlayPause = 1;
    private int stateLikeButton = 1;
    private String trackId = "";

    private SongService songService;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_previous) {
            mSpotifyAppRemote.getPlayerApi().skipPrevious();
            // do something here
        }
        if (id == R.id.action_play) {
            if (stateDrawPlayPause==1) {
                mSpotifyAppRemote.getPlayerApi().resume();
                findViewById(R.id.action_play).setBackgroundResource(android.R.drawable.ic_media_pause);
                stateDrawPlayPause = 0;
            }
            else {
                mSpotifyAppRemote.getPlayerApi().pause();
                findViewById(R.id.action_play).setBackgroundResource(android.R.drawable.ic_media_play);
                stateDrawPlayPause = 1;
            }

        }
        if (id == R.id.action_next) {
            mSpotifyAppRemote.getPlayerApi().skipNext();
        }
        if (id == R.id.action_like) {
            if (stateLikeButton == 0) {
                mSpotifyAppRemote.getUserApi().addToLibrary(trackId);
                findViewById(R.id.action_like).setBackgroundResource(R.drawable.ic_baseline_favorite);
                stateLikeButton = 1;

            } else {
                mSpotifyAppRemote.getUserApi().removeFromLibrary(trackId);
                findViewById(R.id.action_like).setBackgroundResource(R.drawable.ic_baseline_favorite_border);
                stateLikeButton = 1;
            }
        }
        return super.onOptionsItemSelected(item);
    }

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
        btnPlay = findViewById(R.id.btnPlay);

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
        Integer seconds = (song.getRelease()/1000)%60;
        String sec = seconds.toString();
        if (sec.length()==1) sec = '0'+sec;
        lengthSong.setText(min+":"+sec);


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



        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "spotify:track:" + song.getId();
                Log.d(TAG, uri);
                if (uri!=null) {
                    mSpotifyAppRemote.getPlayerApi().play(uri);
                }
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
    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d(TAG, "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private void connected() {
        View actionPlay = findViewById(R.id.action_play);
        ImageView songImage = findViewById(R.id.songImage);
        View likeButton = findViewById(R.id.action_like);
        TextView playing = findViewById(R.id.currentSong);

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        trackId = track.uri;
                        mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(bitmap -> {
                            Glide.with(this)
                                    .load(bitmap)
                                    .placeholder(songImage.getDrawable())
                                    .into(songImage);
                            playing.setText(track.name + "\n" + track.artist.name);
                        });
                    }
                    if (playerState.isPaused) {
                        actionPlay.setBackgroundResource(android.R.drawable.ic_media_play);
                        stateDrawPlayPause = 1;

                    } else {
                        actionPlay.setBackgroundResource(android.R.drawable.ic_media_pause);
                        stateDrawPlayPause = 0;
                    }

                    mSpotifyAppRemote.getUserApi().getLibraryState(trackId).setResultCallback(status -> {
                        if (status.isAdded) {
                            likeButton.setBackgroundResource(R.drawable.ic_baseline_favorite);
                            stateLikeButton = 1;

                        } else {
                            likeButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border);
                            stateLikeButton = 0;
                        }
                    });


                });
    }
//    private void authenticateSpotify() {
//        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
//        builder.setScopes(new String[]{SCOPES});
//        AuthenticationRequest request = builder.build();
//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
//    }
}