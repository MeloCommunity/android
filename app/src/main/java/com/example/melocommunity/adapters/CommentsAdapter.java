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
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.DetailActivity;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;

import org.parceler.Parcels;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvUserName;
        private TextView tvComment;

        private ImageView ivUserImage;

        String imageUserUrl;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);

        }

        public void bind(Comment comment) {
            // Bind the post data to the view elements
            //ivImage.
            tvUserName.setText(comment.getUserName());
            tvComment.setText(comment.getDescription());
            imageUserUrl = comment.getUserImageUrl();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(context)
                    .load(imageUserUrl).apply(options)
                    .into(ivUserImage);

        }
    }
}

