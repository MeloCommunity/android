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

public class SearchSongsAdapter extends RecyclerView.Adapter<SearchSongsAdapter.ViewHolder> {

    private Context context;
    private List<Song> searchSongs;

    public SearchSongsAdapter(Context context, List<Song> searchSongs) {
        this.context = context;
        this.searchSongs = searchSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
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

        private RelativeLayout container;
        private TextView tvSongTitle;
        private TextView tvArtist;

        private ImageView ivSongPoster;

        String imageSongUrl;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongTitle = itemView.findViewById(R.id.tvUserName);
            tvArtist = itemView.findViewById(R.id.tvComment);
            ivSongPoster = itemView.findViewById(R.id.ivSongPoster);
            container = itemView.findViewById(R.id.container);

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

