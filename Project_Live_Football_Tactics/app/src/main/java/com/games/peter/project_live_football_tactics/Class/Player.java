package com.games.peter.project_live_football_tactics.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
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

    protected Player(Parcel in) {
        row = in.readInt();
        col = in.readInt();
        name = in.readString();
        shirtNumber = in.readInt();
        team_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(row);
        dest.writeInt(col);
        dest.writeString(name);
        dest.writeInt(shirtNumber);
        dest.writeInt(team_id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}