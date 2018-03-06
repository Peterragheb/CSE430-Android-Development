package com.games.peter.lab2_matching_game;

/**
 * Created by peter on 3/6/18.
 */

public class User {
    private String username;
    private String Score;

    public User(String username, String score) {
        this.username = username;
        Score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
