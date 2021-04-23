package com.example.melocommunity.models;

import org.w3c.dom.Text;

import java.util.Date;

public class CommentModel {
    public String songId;
    public String commentId;
    public String userId;
    public Date createdAt;
    public Text desciption; //might wanna make this a String

    //we can just create the object and pass in params instead of having manual getters and setters for each field
    public CommentModel(String songId, String commentId, String userId, Date createdAt, Text desciption) {
        this.songId = songId;
        this.commentId = commentId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.desciption = desciption;
    }
}
