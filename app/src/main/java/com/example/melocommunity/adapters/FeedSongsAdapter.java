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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FeedSongsAdapter extends RecyclerView.Adapter<FeedSongsAdapter.ViewHolder> {

    private final Context context;
    private final List<Song> feedSongs;

    public static final String TAG = "FeedSongsAdapter";


    public FeedSongsAdapter(Context context, List<Song> feedSongs) {
        this.context = context;
        this.feedSongs = feedSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song feedSong = feedSongs.get(position);
        holder.bind(feedSong);
    }

    @Override
    public int getItemCount() {
        return feedSongs.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout container;
        private final TextView tvSongTitle;
        private final TextView tvArtist;

        private final ImageView ivSongPoster;
        private final TextView lengthSong2;

        String imageSongUrl;

        private RecyclerView rvComments;

        private CommentsAdapter commentsAdapter;
        private List<Comment> allComments;

        private final ImageView userImage3;
        private final Button btnPost3;
        private final EditText etDescription;
        private String userName;
        private String userID;


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
            userImage3 = itemView.findViewById(R.id.userImage3);
            etDescription = itemView.findViewById(R.id.etEditComment);
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
            userID = (sharedPreferences.getString("userid", "No User"));


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(context)
                    .load(userUrl)
                    .apply(options)
                    .into(userImage3);

            Integer minutes = (feedSong.getRelease()/1000/60);
            String min = minutes.toString();
            if (min.length()==1) min = '0'+min;
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
                }
            });
        }
        }
    }

