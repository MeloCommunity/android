package com.example.melocommunity.models;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.PipedOutputStream;
import java.util.List;

@ParseClassName("Artist")
public class Artist extends ParseObject {
    public static final String KEY_ARTISTID="artistID";
    public static final String KEY_NAME="name";
    public static final String KEY_IMAGE="image";
    public static final String KEY_FEED="feed";
    public static final String KEY_BIOGRAPHY="biography";
    public static final String KEY_MONTHLYLISTENER="monthlyListener";

    public String getArtistID(){
        return getString(KEY_ARTISTID);
    }

    public void setArtistID(String artistID){
        put(KEY_ARTISTID, artistID);
    }

    public String getName(){
        return getString(KEY_NAME);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    public ParseFile getImage(){
        return  getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    //TODO handle this better
    public List<Post> getFeed(){
        return getList(KEY_FEED);
    }

    //TODO handle this better
    public void setFeed(List<Post> posts){
        put(KEY_FEED, posts);
    }

    public String getBiography(){
        return getString(KEY_BIOGRAPHY);
    }

    public void setBiography(String biography){
        put(KEY_BIOGRAPHY, biography);
    }

    public int getMonthlyListener(){
        return getInt(KEY_MONTHLYLISTENER);
    }

    public void setMonthlylistener(int monthlylistener){
        put(KEY_MONTHLYLISTENER, monthlylistener);
    }
}
