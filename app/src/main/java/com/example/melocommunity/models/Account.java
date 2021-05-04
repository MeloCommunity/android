package com.example.melocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Account")
public class Account extends ParseObject {
    public static final String KEY_USERID="userID";
    public static final String KEY_IMAGE="image";
    public static final String KEY_LIKEDSONGS="likedSongs";

    public String getUserID(){
        return getString(KEY_USERID);
    }

    public void setUserID(String userID){
        put(KEY_USERID, userID);
    }

    public ParseFile getImage(){
        return  getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    //TODO handle this better
    public List<Post> getLikedSongs(){
        return getList(KEY_LIKEDSONGS);
    }

    //TODO handle this better
    public void setLikedSongs(List<Post> likedSongs){
        put(KEY_LIKEDSONGS, likedSongs);
    }
}
