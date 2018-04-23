package com.games.peter.project_live_football_tactics;

/**
 * Created by Peter on 18/4/2018.
 */

public class User {
    private String id;
    private String name;
    private String profileUrl;
    User(String id, String name, String profileUrl){
        this.id = id;
        this.name=name;
        this.profileUrl=profileUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}