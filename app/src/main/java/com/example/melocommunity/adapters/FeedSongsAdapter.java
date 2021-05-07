package com.example.melocommunity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melocommunity.R;
import com.example.melocommunity.models.FeedSongs;
import com.example.melocommunity.models.Song;
import com.spotify.protocol.types.Image;

import java.util.List;

public class FeedSongsAdapter extends RecyclerView.Adapter<FeedSongsAdapter.ViewHolder> {

    private Context context;
    private List<Song> feedSongs;

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

        private TextView tvSongTitle;
        private TextView tvArtist;

        private ImageView ivSongPoster;

        String imageSongUrl;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            ivSongPoster = itemView.findViewById(R.id.ivSongPoster);


        }

        public void bind(Song feedSong) {
            // Bind the post data to the view elements
            //ivImage.
            tvSongTitle.setText(feedSong.getName());
            tvArtist.setText(feedSong.getArtist());
            imageSongUrl = feedSong.getImageUrl();

            Glide.with(context)
                    .load(imageSongUrl)
                    .into(ivSongPoster);
            }
        }
    }

