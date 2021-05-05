package com.example.melocommunity.models;

import java.util.ArrayList;


public class User {
    public String birthdate;
    public String country;
    public String display_name;
    public String email;
    public String id;
    public String imageUrl;
    public ArrayList<LikedSongs> likedSongs;


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
