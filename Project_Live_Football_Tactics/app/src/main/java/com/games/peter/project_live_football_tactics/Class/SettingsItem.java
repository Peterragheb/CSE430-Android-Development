package com.games.peter.project_live_football_tactics.Class;

/**
 * Created by Peter on 26/4/2018.
 */

public class SettingsItem {
    private String title;
    private int image_resource;
    //======================================================
    public SettingsItem(String title, int image_resource) {
        this.title = title;
        this.image_resource = image_resource;
    }
    //======================================================
    public String getTitle() {
        return title;
    }
    //======================================================
    public void setTitle(String title) {
        this.title = title;
    }
    //======================================================
    public int getImage_resource() {
        return image_resource;
    }
    //======================================================
    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }
    //======================================================
}
