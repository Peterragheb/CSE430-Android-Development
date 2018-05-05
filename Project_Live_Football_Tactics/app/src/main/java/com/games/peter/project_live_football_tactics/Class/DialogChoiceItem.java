package com.games.peter.project_live_football_tactics.Class;

public class DialogChoiceItem {
    private int imageResource;
    private String name;
    private int favorite_team_id;
    //======================================================
    public DialogChoiceItem(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }
    //======================================================
    public DialogChoiceItem(int imageResource, String name , int favorite_team_id) {
        this.imageResource = imageResource;
        this.name = name;
        this.favorite_team_id = favorite_team_id;
    }
    //======================================================
    public int getImageResource() {
        return imageResource;
    }
    //======================================================
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
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
    public int getFavorite_team_id() {
        return favorite_team_id;
    }
    //======================================================
    public void setFavorite_team_id(int favorite_team_id) {
        this.favorite_team_id = favorite_team_id;
    }
    //======================================================
}
