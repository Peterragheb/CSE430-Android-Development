package com.games.peter.project_live_football_tactics;

import java.util.Date;

/**
 * Created by Peter on 19/4/2018.
 */

public class Match {
    private boolean started;
    private Date start_time;
    private int minute;
    private String home_team;
    private String away_team;
    private int home_score;
    private int away_score;

    public Match(int minute, String home_team, String away_team, int home_score, int away_score) {
        this.started = true;
        this.start_time = null;
        this.minute = minute;
        this.home_team = home_team;
        this.away_team = away_team;
        this.home_score = home_score;
        this.away_score = away_score;
    }
    public Match(Date start_time, String home_team, String away_team) {
        this.started = false;
        this.start_time = start_time;
        this.minute = 0;
        this.home_team = home_team;
        this.away_team = away_team;
        this.home_score = 0;
        this.away_score = 0;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Date getStarttime() {
        return start_time;
    }

    public void setStarttime(Date starttime) {
        this.start_time = starttime;
    }

    public String getHome_team() {
        return home_team;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }

    public int getHome_score() {
        return home_score;
    }

    public void setHome_score(int home_score) {
        this.home_score = home_score;
    }

    public int getAway_score() {
        return away_score;
    }

    public void setAway_score(int away_score) {
        this.away_score = away_score;
    }
}
