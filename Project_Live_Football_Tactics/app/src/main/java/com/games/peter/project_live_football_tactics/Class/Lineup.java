package com.games.peter.project_live_football_tactics.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Lineup implements Parcelable {
    private ArrayList<Player> players;
    //======================================================
    public Lineup(){
        players = new ArrayList<>();
    }
    //======================================================
    public ArrayList<Player> getPlayers() {
        return players;
    }
    //======================================================
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    //======================================================
    public int getPlayersSize(){
        return players.size();
    }
    //======================================================
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    //======================================================
    protected Lineup(Parcel in) {
        if (in.readByte() == 0x01) {
            players = new ArrayList<Player>();
            in.readList(players, Player.class.getClassLoader());
        } else {
            players = null;
        }
    }
    //======================================================
    @Override
    public int describeContents() {
        return 0;
    }
    //======================================================
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (players == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(players);
        }
    }
    //======================================================
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Lineup> CREATOR = new Parcelable.Creator<Lineup>() {
        @Override
        public Lineup createFromParcel(Parcel in) {
            return new Lineup(in);
        }

        @Override
        public Lineup[] newArray(int size) {
            return new Lineup[size];
        }
    };
    //======================================================
}
