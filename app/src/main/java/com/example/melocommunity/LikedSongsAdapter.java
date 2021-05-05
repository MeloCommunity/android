package com.example.melocommunity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melocommunity.models.LikedSongs;
import com.spotify.protocol.types.Image;

import java.util.List;

public class LikedSongsAdapter extends RecyclerView.Adapter<LikedSongsAdapter.ViewHolder> {

    private Context context;
    private List<LikedSongs> likedSongs;

    public LikedSongsAdapter(Context context, List<LikedSongs> likedSongs) {
        this.context = context;
        this.likedSongs = likedSongs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LikedSongs likedSong = likedSongs.get(position);
        holder.bind(likedSong);
    }

    @Override
    public int getItemCount() {
        return likedSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // 1st inner LinearLayout
        private ImageView ivImage;
        private ImageView ivPlaySong;

        // 2nd inner LinearLayout
        private TextView tvSongName;
        private TextView tvArtistName;

        private ImageButton btnComments;

        // 3rd inner layout
        private TextView tvCommentSection;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            ivPlaySong = itemView.findViewById(R.id.ivPlaySong);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            btnComments = itemView.findViewById(R.id.btnComments);
            tvCommentSection = itemView.findViewById(R.id.tvCommentSection);

        }

        public void bind(LikedSongs likedSong) {
            // Bind the post data to the view elements
            //ivImage.
            tvSongName.setText(likedSong.nameSong);
            tvArtistName.setText(likedSong.nameArtist);
            Image image = likedSong.imageSong;

            if(image != null) {
                Glide.with(context).load(image).into(ivImage);
            }
        }
    }
}
