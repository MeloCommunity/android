package com.example.melocommunity.models;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Iterator;

@Parcel
public class Song {

    private String id;
    private String name;
    private String uri;
    private String artist;
    public String imageUrl;
    public int Release;


    public int getRelease() {
        return Release;
    }
    public void setRelease(int release) {
        Release = release;
    }

    //empty constructor needed by the Parceler library
    public Song() {}

    public Song(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getUri() {
        return uri;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getArtist() {
        return artist;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() { return imageUrl; }
}