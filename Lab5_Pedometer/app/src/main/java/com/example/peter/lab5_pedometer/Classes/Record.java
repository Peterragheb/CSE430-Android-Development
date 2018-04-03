package com.example.peter.lab5_pedometer.Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peter on 30/3/2018.
 */

public class Record {
    private Date date;
    private int steps;
    private float calories;
    private float distance;
    private float speed;
    public Record(Date date, int steps, float calories, float distance,  float speed) {
        this.date = date;
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.speed = speed;
    }
    public Record(SimpleDateFormat sdf, String date, int steps, float calories, float distance,  float speed) {
        if (sdf==null)
            sdf=new SimpleDateFormat("EEE, MMM d");
        try {
            this.date = sdf.parse(date);
            this.steps = steps;
            this.calories = calories;
            this.distance = distance;
            this.speed = speed;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
