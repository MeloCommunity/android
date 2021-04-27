package com.example.melocommunity.models;

import org.w3c.dom.Text;

import java.util.Date;

public class Comment {
    public String songId;
    public String commentId;
    public String userId;
    public Date createdAt;
    public Text description; //might wanna make this a String

    //we can just create the object and pass in params instead of having manual getters and setters for each field
    public Comment(String songId, String commentId, String userId, Date createdAt, Text desciption) {
        this.songId = songId;
        this.commentId = commentId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.description = description;
    }

    public String getId() {
        return commentId;
    }

    public void setId(String id) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSongIdr() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text Description) {
        this.description = description;
    }
}
