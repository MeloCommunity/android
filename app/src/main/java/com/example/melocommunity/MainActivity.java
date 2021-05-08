package com.example.melocommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.melocommunity.Connectors.UserService;
import com.example.melocommunity.fragments.AccountFragment;
import com.example.melocommunity.fragments.FeedFragment;
import com.example.melocommunity.fragments.SearchFragment;
import com.example.melocommunity.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class MainActivity extends AppCompatActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    private static final String CLIENT_ID =  "676d4db0d44b4f95956d8efa0ff25ff8";
    private static final String REDIRECT_URI = "com.example.melocommunity://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private, user-top-read, app-remote-control";

    private SpotifyAppRemote mSpotifyAppRemote;
    private int stateDrawPlayPause = 1;
    private int stateLikeButton = 1;
    private String trackId = null;



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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_account:
                        fragment = new AccountFragment();
                        break;
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_feed:
                    default:
                        fragment = new FeedFragment();
                        break;

                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_account);
    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();

                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("display_name", user.display_name);
            editor.putString("userid", user.id);
            editor.putString("email", user.email);
            editor.putString("country", user.country);
            editor.putString("imageUrl", user.imageUrl);
            Log.d("STARTING", "GOT USER INFORMATION");
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
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
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

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
                        playing.setText(track.name + " by " + track.artist.name);
                        mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(bitmap -> {
                            Glide.with(this)
                                    .load(bitmap)
                                    .into(songImage);
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
}