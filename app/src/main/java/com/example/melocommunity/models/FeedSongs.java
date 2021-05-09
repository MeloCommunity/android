package com.example.melocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.spotify.protocol.types.Image;

import java.util.Date;

@ParseClassName("FeedSongs")
public class FeedSongs extends ParseObject {

    public static final String KEY_SONG_ID = "songID";
    public static final String KEY_NAME_SONG = "nameSong";
    public static final String KEY_NAME_ARTIST = "nameArtist";
    public static final String KEY_IMAGE_SONG = "imageSong";
    //public static final String KEY_CREATED_AT = "createdAt";

    //songID
    public String getSongID() {
        return getString(KEY_SONG_ID);
    }

    public void setSongID(String songID) {
        put(KEY_SONG_ID, songID);
    }

    //nameSong
    public String getNameSong() {
        return getString(KEY_NAME_SONG);
    }

    public void setNameSong(String nameSong) {
        put(KEY_NAME_SONG, nameSong);
    }

    //nameArtist
    public String getNameArtist() {
        return getString(KEY_NAME_ARTIST);
    }

    public void setNameArtist(String nameArtist) {
        put(KEY_NAME_ARTIST, nameArtist);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE_SONG);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE_SONG, parseFile);
    }
}

