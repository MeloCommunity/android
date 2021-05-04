package com.example.melocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_SONGID="songID";
    public static final String KEY_USERID="userID";
    public static final String KEY_DESCRIPTION="description";

    public String getSongID(){
        return getString(KEY_SONGID);
    }

    public void setSongID(String songID){
        put(KEY_SONGID, songID);
    }

    //TODO handle this better
    public String getCommentID(){
        return getObjectId();
    }

    public String getUserID(){
        return getString(KEY_USERID);
    }

    public void setUserID(String userID){
        put(KEY_USERID, userID);
    }

    //TODO handle this better
    public Date getPostCreatedAt(){
        return getCreatedAt();
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }
}
