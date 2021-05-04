package com.example.melocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_SONGID="songID";
    public static final String KEY_NAMESONG="nameSong";
    public static final String KEY_NAMEARTIST="nameArtist";
    public static final String KEY_NAMEALBUM="nameAlbum";
    public static final String KEY_IMAGE="image";
    public static final String KEY_COMMENTS="comments";
    public static final String KEY_STREAMSCOUNT="streamsCount";

    public String getSongID(){
        return getString(KEY_SONGID);
    }

    public void setSongID(String songID){
        put(KEY_SONGID, songID);
    }

    public String getNameSong(){
        return getString(KEY_NAMESONG);
    }

    public void setNameSong(String nameSong){
        put(KEY_NAMESONG, nameSong);
    }

    public String getNameArtist(){
        return getString(KEY_NAMEARTIST);
    }

    public void setNameArtist(String nameArtist){
        put(KEY_NAMEARTIST, nameArtist);
    }

    public String getNameAlbum(){
        return getString(KEY_NAMEALBUM);
    }

    public void setNameAlbum(String nameAlbum){
        put(KEY_NAMEALBUM, nameAlbum);
    }

    public ParseFile getImage(){
        return  getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    //TODO handle this better
    public List<Comment> getComments(){
        return getList(KEY_COMMENTS);
    }

    //TODO handle this better
    public void setComments(List<Comment> comments){
        put(KEY_COMMENTS, comments);
    }

    public int getStreamsCount(){
        return getInt(KEY_STREAMSCOUNT);
    }

    public void setStreamsCount(int streamsCount){
        put(KEY_STREAMSCOUNT, streamsCount);
    }

    //TODO handle this better
    public Date getPostCreatedAt(){
        return getCreatedAt();
    }
}
