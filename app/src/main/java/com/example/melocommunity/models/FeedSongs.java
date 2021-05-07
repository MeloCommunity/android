package com.example.melocommunity.models;

import com.spotify.protocol.types.Image;

import java.util.Date;

public class FeedSongs {

    public String songID;
    public String nameSong;
    public String nameArtist;
    public Image imageSong;
    public Date createdAt;

    public FeedSongs(String songID, String nameSong, String nameArtist,
                     Image imageSong, Date createdAt) {

        this.songID = songID;
        this.nameSong = nameSong;
        this.nameArtist = nameArtist;
        this.imageSong = imageSong;
        this.createdAt = createdAt;
    }
}
