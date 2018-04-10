package com.games.peter.lab6_money_collector_game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peter on 30/3/2018.
 */

public class Record {
    private Date date;
    private int score;
    private int coins;

    public Record(Date date, int score, int coins) {
        this.date = date;
        this.score = score;
        this.coins = coins;
    }
    public Record(SimpleDateFormat sdf, String date, int score, int coins) {
        if (sdf==null)
            sdf=new SimpleDateFormat("EEE, MMM d");
        try {
            this.date = sdf.parse(date);
            this.score = score;
            this.coins = coins;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setDate(String date ,SimpleDateFormat sdf) {
        if (sdf==null)
            sdf=new SimpleDateFormat("EEE, MMM d");
        try {
            this.date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String parseDate(SimpleDateFormat sdf){
        if (sdf==null)
            sdf=new SimpleDateFormat("EEE, MMM d");
       return sdf.format(date);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
