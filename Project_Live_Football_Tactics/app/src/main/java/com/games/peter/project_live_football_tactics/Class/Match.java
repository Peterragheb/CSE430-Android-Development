package com.games.peter.project_live_football_tactics.Class;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;

/**
 * Created by Peter on 19/4/2018.
 */

public class Match implements Comparable<Match> {
    private ArrayList<ChangeListener> listeners;
    private String fixture_id;
    private String id_season;
    private String fixture_status_short;
    private String id_team_season_away;
    private String id_team_season_home;
    private String number_goal_team_away;
    private String number_goal_team_home;
    private String schedule_date;
    private String team_season_away_name;
    private String team_season_home_name;
    private String lineup_confirmed;
    private String elapsed;
    private ArrayList<Lineup> lineups;
    //======================================================
    public Match(String fixture_id, String id_season, String fixture_status_short,
                 String id_team_season_away, String id_team_season_home, String number_goal_team_away,
                 String number_goal_team_home, String schedule_date, String team_season_away_name,
                 String team_season_home_name, String lineup_confirmed, String elapsed) {

        this.fixture_id = fixture_id;
        this.id_season = id_season;
        this.fixture_status_short = fixture_status_short;
        this.id_team_season_away = id_team_season_away;
        this.id_team_season_home = id_team_season_home;
        this.number_goal_team_away = number_goal_team_away;
        this.number_goal_team_home = number_goal_team_home;
        this.schedule_date = schedule_date;
        this.team_season_away_name = team_season_away_name;
        this.team_season_home_name = team_season_home_name;
        this.lineup_confirmed = lineup_confirmed;
        this.elapsed = elapsed;
        listeners = new ArrayList<>();
        lineups = new ArrayList<>();
    }
    //======================================================
    public ArrayList<ChangeListener> getListenerList() {
        return listeners;
    }
    //======================================================
    public ChangeListener getListenerFromList(int i) {
        return listeners.get(i);
    }
    //======================================================
    public void addListener(ChangeListener listener) {
        this.listeners.add(listener) ;
    }
    //======================================================
    public void removeListener(ChangeListener listener){
        Log.v("removeListener","entered");
        this.listeners.remove(listener);
    }
    //======================================================
    public String getFixture_id() {
        return fixture_id;
    }
    //======================================================
    public void setFixture_id(String fixture_id) {
        this.fixture_id = fixture_id;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("fixture_id",fixture_id);
            }
        }
    }
    //======================================================
    public String getId_season() {
        return id_season;
    }
    //======================================================
    public void setId_season(String id_season) {
        this.id_season = id_season;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("id_season" ,id_season);
            }
        }

    }
    //======================================================
    public String getFixture_status_short() {
        return fixture_status_short;
    }
    //======================================================
    public void setFixture_status_short(String fixture_status_short) {
        this.fixture_status_short = fixture_status_short;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++) {
                listeners.get(i).onChange("fixture_status_short",fixture_status_short);
            }
            }

    }
    //======================================================
    public String getId_team_season_away() {
        return id_team_season_away;
    }
    //======================================================
    public void setId_team_season_away(String id_team_season_away) {
        this.id_team_season_away = id_team_season_away;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++) {
                listeners.get(i).onChange("id_team_season_away",id_team_season_away);
            }
        }

    }
    //======================================================
    public String getId_team_season_home() {
        return id_team_season_home;
    }
    //======================================================
    public void setId_team_season_home(String id_team_season_home) {
        this.id_team_season_home = id_team_season_home;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("id_team_season_home",id_team_season_home);
            }
        }

    }
    //======================================================
    public String getNumber_goal_team_away() {
        return number_goal_team_away;
    }
    //======================================================
    public void setNumber_goal_team_away(String number_goal_team_away) {
        this.number_goal_team_away = number_goal_team_away;
        Log.v("set_goal_team_away","Entered");
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                Log.v("set_goal_team_away","linstnercalled");
                listeners.get(i).onChange("number_goal_team_away",number_goal_team_away);
            }
        }
    }
    //======================================================
    public String getNumber_goal_team_home() {
        return number_goal_team_home;
    }
    //======================================================
    public void setNumber_goal_team_home(String number_goal_team_home) {
        this.number_goal_team_home = number_goal_team_home;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("number_goal_team_home",number_goal_team_home);
            }
        }

    }
    //======================================================
    public String getSchedule_date() {
        return schedule_date;
    }
    //======================================================
    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("schedule_date",schedule_date);
            }
        }

    }

    public String getTeam_season_away_name() {
        return team_season_away_name;
    }
    //======================================================
    public void setTeam_season_away_name(String team_season_away_name) {
        this.team_season_away_name = team_season_away_name;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("team_season_away_name",team_season_away_name);
            }
        }
    }

    public String getTeam_season_home_name() {
        return team_season_home_name;
    }
    //======================================================
    public void setTeam_season_home_name(String team_season_home_name) {
        this.team_season_home_name = team_season_home_name;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("team_season_home_name",team_season_home_name);
            }
        }

    }
    //======================================================
    public String getLineup_confirmed() {
        return lineup_confirmed;
    }
    //======================================================
    public void setLineup_confirmed(String lineup_confirmed) {
        this.lineup_confirmed = lineup_confirmed;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("lineup_confirmed",lineup_confirmed);
            }
        }

    }
    //======================================================
    public String getElapsed() {
        return elapsed;
    }
    //======================================================
    public void setElapsed(String elapsed) {
        this.elapsed = elapsed;
        if (listeners != null){
            for (int i=0;i<listeners.size();i++){
                listeners.get(i).onChange("elapsed",elapsed);
            }
        }

    }
    //======================================================
    public static ArrayList<Match> sortMatchesBy_ScheduledDate(ArrayList<Match> matches){
        Collections.sort(matches);
        return matches;
    }
    //======================================================
    @Override
    public int compareTo(@NonNull Match o) {
        SimpleDateFormat serversdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        serversdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
           return serversdf.parse(getSchedule_date()).compareTo(serversdf.parse(o.getSchedule_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }
    //======================================================
    public ArrayList<Lineup> getLineups() {
        return lineups;
    }
    //======================================================
    public void setLineups(ArrayList<Lineup> lineups) {
        this.lineups = lineups;
    }
    //======================================================
    public interface ChangeListener {
        void onChange(String field,String value);
    }
    //======================================================
}
