package com.games.peter.project_live_football_tactics.Class;

/**
 * Created by Peter on 18/4/2018.
 */

public class Message {
    private User sender;
    private long createdAt;
    private String message;
    //======================================================
    public Message(User sender, long createdAt, String message) {
        this.sender = sender;
        this.createdAt = createdAt;
        this.message = message;
    }
    //======================================================
    public String getMessage() {
        return message;
    }
    //======================================================
    public void setMessage(String message) {
        this.message = message;
    }
    //======================================================
    public User getSender() {
        return sender;
    }
    //======================================================
    public void setSender(User sender) {
        this.sender = sender;
    }
    //======================================================
    public long getCreatedAt() {
        return createdAt;
    }
    //======================================================
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    //======================================================
}
