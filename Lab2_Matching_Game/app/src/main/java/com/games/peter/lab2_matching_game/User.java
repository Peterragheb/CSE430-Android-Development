package com.games.peter.lab2_matching_game;

import android.support.annotation.NonNull;

/**
 * Created by peter on 3/6/18.
 */

public class User  implements Comparable<User>{
    private String username;
    private int score;
    private String score_type;
    private String date;
    public User(String username, int score, String score_type, String date) {
        this.username = username;
        this.score = score;
        this.score_type = score_type;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getScore_type() {
        return score_type;
    }

    public void setScore_type(String score_type) {
        this.score_type = score_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull User user) {
        if (this.getScore_type().equals(user.getScore_type()))
            return this.getScore()-user.getScore();
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_1)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_2))
            return -1;
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_1)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_3))
            return -1;
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_2)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_1))
            return 1;
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_2)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_3))
            return -1;
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_3)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_1))
            return 1;
        else if (this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_3)&&this.getScore_type().equals(MainActivity.SCORE_TYPE_MESSAGE_2))
            return 1;
        return 0;
    }
}
