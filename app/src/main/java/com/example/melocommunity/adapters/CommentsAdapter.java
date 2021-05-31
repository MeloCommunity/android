package com.example.melocommunity.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.melocommunity.R;
import com.example.melocommunity.models.Comment;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
        ImageView btnEdit;

        // Views inside the edit alert dialog
        EditText etEditComment;
        Button btnPostEdit;
        Button btnCancelEdit;
        String userID;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivUserImage = itemView.findViewById(R.id.ivUserImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("SPOTIFY", 0);

            userID = (sharedPreferences.getString("userid", "No User"));

        }

        public void bind(Comment comment) {
            if (comment.getUserID().equals(userID)) {
                btnDelete.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.VISIBLE);
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
                                        comments.remove(comment);
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

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Edit Comment");
                    alertDialogBuilder.setCancelable(true);
                    //We are setting our custom popup view by AlertDialog.Builder
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.edit_alert_dialogbox, null);
                    alertDialogBuilder.setView(dialogView);
                    etEditComment = dialogView.findViewById(R.id.etEditComment);
                    btnPostEdit = dialogView.findViewById(R.id.btnPostEdit);
                    btnCancelEdit = dialogView.findViewById(R.id.btnCancelEdit);
                    etEditComment.setText(comment.getDescription());

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    btnPostEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etEditComment.getText().toString().isEmpty()){
                                Toast.makeText(context, "Cannot post empty comment", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                SaveEdit(etEditComment.getText().toString());
                                alertDialog.cancel();
                                comment.setDescription(etEditComment.getText().toString());
                                notifyDataSetChanged();
                            }
                        }
                    });

                    btnCancelEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });


                }

                private void SaveEdit(String newDescription) {
                    if (comment.getUserID().equals(userID)){
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");

                        // Retrieve the object by id
                        query.getInBackground(comment.getObjectId(), (object, e) -> {
                            if (e == null) {
                                // Update the fields we want to
                                object.put("description", newDescription);

                                // All other fields will remain the same
                                object.saveInBackground();

                            } else {
                                // something went wrong
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });


        }

    }

}

