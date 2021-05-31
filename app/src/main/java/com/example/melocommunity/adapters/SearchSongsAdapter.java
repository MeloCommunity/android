package com.example.melocommunity.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.DetailActivity;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SearchSongsAdapter extends RecyclerView.Adapter<SearchSongsAdapter.ViewHolder> {

    private final Context context;
    private final List<Song> searchSongs;

    public static final String TAG = "SearchSongAdapter";

    private static final String CLIENT_ID =  "676d4db0d44b4f95956d8efa0ff25ff8";
    private static final String REDIRECT_URI = "com.example.melocommunity://callback";

    protected SpotifyAppRemote mSpotifyAppRemote;

    public SearchSongsAdapter(Context context, List<Song> searchSongs) {
        this.context = context;
        this.searchSongs = searchSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this.context, connectionParams,
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song searchSong = searchSongs.get(position);
        holder.bind(searchSong);
    }

    @Override
    public int getItemCount() {
        return searchSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout container;
        private final TextView tvSongTitle;
        private final TextView tvArtist;

        private final ImageView ivSongPoster;
        private final TextView lengthSong2;

        private final ImageView btnPlay2;

        String imageSongUrl;

        private RecyclerView rvComments;

        private CommentsAdapter commentsAdapter;
        private List<Comment> allComments;


        private final ImageView btnPost3;
        private final EditText etDescription;
        private String userName;

        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongTitle = itemView.findViewById(R.id.tvUserName);
            tvArtist = itemView.findViewById(R.id.tvComment);
            ivSongPoster = itemView.findViewById(R.id.ivSongPoster);
            container = itemView.findViewById(R.id.container);
            lengthSong2 = itemView.findViewById(R.id.lengthSong2);
            btnPost3 = itemView.findViewById(R.id.btnPost3);
            etDescription = itemView.findViewById(R.id.tiComment);
            btnPlay2 = itemView.findViewById(R.id.btnPlay2);

        }

        public void bind(Song feedSong) {
            // Bind the post data to the view elements
            //ivImage.
            tvSongTitle.setText(feedSong.getName());
            tvArtist.setText(feedSong.getArtist());
            imageSongUrl = feedSong.getImageUrl();

            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("SPOTIFY", 0);
            String userUrl = sharedPreferences.getString("imageUrl", "No User");
            userName = (sharedPreferences.getString("display_name", "No User"));
            String userID = (sharedPreferences.getString("userid", "No User"));


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);


            Integer minutes = (feedSong.getRelease()/1000/60);
            String min = minutes.toString();
            Integer seconds = (feedSong.getRelease()/1000)%60;
            String sec = seconds.toString();
            if (sec.length()==1) sec = '0'+sec;
            lengthSong2.setText(min+":"+sec);

            Glide.with(context)
                    .load(imageSongUrl)
                    .into(ivSongPoster);

            rvComments = itemView.findViewById(R.id.rvComments);

            rvComments.addOnItemTouchListener(mScrollTouchListener);

            allComments = new ArrayList<>();
            commentsAdapter = new CommentsAdapter(context, allComments);

            // Steps to use the recycler view:
            // 0. create layout for one row in the list
            // 1. create the adapter
            // 2. create the data source
            // 3. set the adapter on the recycler view
            rvComments.setAdapter(commentsAdapter);
            // 4. set the layout manager on the recycler view
            rvComments.setLayoutManager(new LinearLayoutManager(context));
            queryComments(feedSong.getId());

            //On click that sends the information to postComment
            btnPost3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String description = etDescription.getText().toString();
                    if (description.isEmpty()) {
                        return;
                    }
                    postComment(description, userName, userUrl, feedSong.getId(), userID);
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("Song", Parcels.wrap(feedSong));
                    context.startActivity(i);
                }
            });

            btnPlay2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uri = "spotify:track:" + feedSong.getId();
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
            comment.setDescription(description);
            comment.setUserName(userName);
            comment.setUserID(userID);
            comment.setUserImageUrl(userImageUrl);
            comment.saveInBackground(e -> {
                if (e==null){
                    allComments.add(comment);
                    commentsAdapter.notifyDataSetChanged();
                    etDescription.setText("");
                    //Save was done
                }else{
                    //Something went wrong
                }
            });
        }
    }
    private void connected() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                });
    }
}

