package com.example.melocommunity.models;

import com.spotify.protocol.types.Image;

import java.util.ArrayList;

public class User {

    public String userID;
    public Image userImage;
    public ArrayList<LikedSongs> likedSongs;

    public User(String userID, Image userImage, ArrayList<LikedSongs> likedSongs) {
        this.userID = userID;
        this.userImage = userImage;
        this.likedSongs = likedSongs;
    }
}
