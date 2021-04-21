package com.example.melocommunity.models;

public class Comment {
    /*
    * songId, String, what song is it about
commentId,String, comment’s unique id
userId, string, user who left the comment
createdAt, datetime, time comment was posted
Description, text, the actual comment
    * */

    private String commentId;
    private String songId;
    private User user;
    private String createdAt;
    private String body; //description

    public Comment(String commentId, String songId, User user, String body) {
        this.commentId = commentId;
        this.songId = songId;
        this.user = user;
        this.body = body;
    }

    public String getId() {
        return commentId;
    }

    public void setId(String id) {
        this.commentId = commentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSongIdr() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
