package com.games.peter.project_live_football_tactics.Class;

public class Player {
    private int row;
    private int col;
    private String name;
    private int shirtNumber;
    private int team_id;
    //======================================================
    public Player(int row, int col, String name, int shirtNumber, int team_id) {
        this.row = row;
        this.col = col;
        this.name = name;
        this.shirtNumber = shirtNumber;
        this.team_id = team_id;
    }
    //======================================================
    public int getRow() {
        return row;
    }
    //======================================================
    public void setRow(int row) {
        this.row = row;
    }
    //======================================================
    public int getCol() {
        return col;
    }
    //======================================================
    public void setCol(int col) {
        this.col = col;
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
    public int getShirtNumber() {
        return shirtNumber;
    }
    //======================================================
    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }
    //======================================================
    public int getTeam_id() {
        return team_id;
    }
    //======================================================
    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }
    //======================================================
}
