package com.example.melocommunity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melocommunity.DetailActivity;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Song;

import org.parceler.Parcels;

import java.util.List;

public class FeedSongsAdapter extends RecyclerView.Adapter<FeedSongsAdapter.ViewHolder> {

    private final Context context;
    private final List<Song> feedSongs;

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



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongTitle = itemView.findViewById(R.id.tvUserName);
            tvArtist = itemView.findViewById(R.id.tvComment);
            ivSongPoster = itemView.findViewById(R.id.ivSongPoster);
            container = itemView.findViewById(R.id.container);
            lengthSong2 = itemView.findViewById(R.id.lengthSong2);


        }

        public void bind(Song feedSong) {
            // Bind the post data to the view elements
            //ivImage.
            tvSongTitle.setText(feedSong.getName());
            tvArtist.setText(feedSong.getArtist());
            imageSongUrl = feedSong.getImageUrl();

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

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("Song", Parcels.wrap(feedSong));
                    context.startActivity(i);
                }
            });
            }
        }
    }

