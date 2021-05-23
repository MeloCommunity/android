package com.example.melocommunity.models;

import java.util.ArrayList;


public class User {
    public String birthdate;
    public String country;
    public String display_name;
    public String email;

    public String getId() {
        return id;
    }

    public String id;
    public String imageUrl;
    public ArrayList<FeedSongs> feedSongs;


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setId(String id) {
        this.id = id;
    }
}
