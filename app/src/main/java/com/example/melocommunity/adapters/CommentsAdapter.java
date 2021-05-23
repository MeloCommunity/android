package com.example.melocommunity.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.DetailActivity;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Comment;
import com.example.melocommunity.models.Song;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final Context context;
    private final List<Comment> comments;

    public static final String TAG = "CommentsAdapter";

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


        private final TextView tvUserName;
        private final TextView tvComment;

        private final ImageView ivUserImage;

        String imageUserUrl;

        ImageView btnDelete;
        String userID;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("SPOTIFY", 0);

            userID = (sharedPreferences.getString("userid", "No User"));

        }

        public void bind(Comment comment) {
            if (comment.getUserID().equals(userID)) {
                btnDelete.setVisibility(View.VISIBLE);
            }
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

            if (comment.getUserID().equals(userID)) {
                btnDelete.setVisibility(View.VISIBLE);
            }

            Log.d(TAG, userID + " " + comment.getUserID());


            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (comment.getUserID().equals(userID)) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
                        query.getInBackground(comment.getObjectId(), (object, e) -> {
                            if (e == null) {
                                //Object was fetched
                                //Deletes the fetched ParseObject from the database
                                object.deleteInBackground(e2 -> {
                                    if(e2==null & comment.getUserID().equals(userID)){
                                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }else{
                                        //Something went wrong while deleting the Object
                                        Toast.makeText(context, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                //Something went wrong
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            });


        }
    }
}

