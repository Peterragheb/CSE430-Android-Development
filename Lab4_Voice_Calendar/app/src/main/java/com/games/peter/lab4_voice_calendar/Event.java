package com.games.peter.lab4_voice_calendar;

/**
 * Created by Peter on 24/3/2018.
 */

public class Event {
    private long id;
    private String Name;
    private String Date;
    private String Time;

    public Event(long id, String name, String date, String time) {
        this.id = id;
        this.Name = name;
        this.Date = date;
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
