package com.example.melocommunity.models;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class User {
    public String birthdate;
    public String country;
    public String display_name;
    public String email;
    public String id;
    public String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
