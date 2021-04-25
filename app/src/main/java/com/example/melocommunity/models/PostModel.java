package com.example.melocommunity.models;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

public class PostModel {
    public String songID;
    public String nameSong;
    public String nameAlbum;
    public Image image; //might wanna make this 64 encoded
    public ArrayList<CommentModel> comments;
    public int streamsCount;
    public Date createdAt;

    //we can just create the object and pass in params instead of having manual getters and setters for each field
    public PostModel(String songID, String nameSong, String nameAlbum, Image image, ArrayList<CommentModel> comments, int streamsCount, Date createdAt) {
        this.songID = songID;
        this.nameSong = nameSong;
        this.nameAlbum = nameAlbum;
        this.image = image;
        this.comments = comments;
        this.streamsCount = streamsCount;
        this.createdAt = createdAt;
    }


}
