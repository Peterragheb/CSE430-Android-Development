package com.games.peter.project_live_football_tactics.Class;

/**
 * Created by Peter on 18/4/2018.
 */

public class User {
    private String id;
    private String name;
    private String profilePictureUrl;
    private String favorite_team;
    //======================================================
    public User(String id, String name, String profilePictureUrl){
        this.id = id;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }
    //======================================================
    public User(String id, String name, String profilePictureUrl, String favorite_team){
        this.id = id;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
        this.favorite_team = favorite_team;
    }
    //======================================================
    public String getName() {
        return name;
    }
    //======================================================
    public void setName(String name) {
        this.name = name;
    }
    //======================================================
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    //======================================================
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    //======================================================
    public String getId() {
        return id;
    }
    //======================================================
    public void setId(String id) {
        this.id = id;
    }
    //======================================================
    public String getFavorite_team() {
        return favorite_team;
    }
    //======================================================
    public void setFavorite_team(String favorite_team) {
        this.favorite_team = favorite_team;
    }
    //======================================================
}