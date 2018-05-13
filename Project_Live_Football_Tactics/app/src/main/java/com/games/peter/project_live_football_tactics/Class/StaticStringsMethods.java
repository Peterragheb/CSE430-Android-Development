package com.games.peter.project_live_football_tactics.Class;

import com.games.peter.project_live_football_tactics.R;

import java.util.ArrayList;
import java.util.Date;

public final class StaticStringsMethods {
    public final static String PREMIER_LEAGUE = "Premier League";
    public final static String LA_LIGA = "La Liga";
    public final static String BUNDES_LIGA = "BundesLiga";
    public final static String SERIA_A ="Seria A";
    public final static String LIGUE_1 ="Ligue 1";
    public final static String REFRESH_TOKEN ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YWQ2MWI1OTM0OGFhNzA3MGQwNjg3MzYiLCJpYXQiOjE1MjQ1MjQxMTd9.YloIBVTLovSCP-OoHfgVEfgipCFra8_Yzo9nqmHd3K8";
    public final static String ACCESS_TOKEN_REQUEST = "https://api.sportdeer.com/v1/accessToken?refresh_token=";
    public final static String ACCESS_TOKEN_FIELD = "access_token=";
    public final static String REQUEST_IN_LEAGUES = "https://api.sportdeer.com/v1/leagues/";
    public final static String REQUEST_IN_SEASONS = "https://api.sportdeer.com/v1/seasons/";
    public final static String REQUEST_IN_FIXTURES = "https://api.sportdeer.com/v1/fixtures/";
    public final static String REQUEST_ALL_SEASONS = "/seasons?";
    public final static String REQUEST_ALL_TEAMS = "/teamSeasons?";
    public final static String REQUEST_FIXTURES = "/fixtures?";
    public final static String REQUEST_LINEUPS = "/lineups?";
    public final static String DATE_FROM_FIELD = "dateFrom=";
    public final static String DATE_TO_FIELD = "dateTo=";
    public final static String PAGE_FIELD = "page=";
    public final static String CONTRY_ENGLAND = "46";
    public final static String CONTRY_FRANCE = "54";
    public final static String CONTRY_GERMANY = "59";
    public final static String CONTRY_ITALY = "86";
    public final static String CONTRY_SPAIN = "122";
    public final static String LEAGUE_ENGLAND = "8";
    public final static String LEAGUE_FRANCE = "123";
    public final static String LEAGUE_GERMANY = "108";
    public final static String LEAGUE_ITALY = "101";
    public final static String LEAGUE_SPAIN = "129";
    public final static String IS_LAST_SEASON = "is_last_season";
    public final static String NOT_STARTED = "NS";
    public final static String STARTED = "ST";
    public final static String FINISHED = "FIN";
    public final static String HALF_TIME = "HT";
    public final static String NO_LINEUP = "no_lineup";
    public final static String FAVORITE_TEAM = "favorite_team";
    public final static String FAVORITE_TEAM_ID = "favorite_team_id";
    //======================================================
    public static String getLeagueId(String name) {
        switch (name){
            case PREMIER_LEAGUE:
                return StaticStringsMethods.LEAGUE_ENGLAND;
            case LIGUE_1 :
                return StaticStringsMethods.LEAGUE_FRANCE;
            case SERIA_A :
                return StaticStringsMethods.LEAGUE_ITALY;
            case BUNDES_LIGA :
                return StaticStringsMethods.LEAGUE_GERMANY;
            case LA_LIGA :
                return StaticStringsMethods.LEAGUE_SPAIN;
            default:
                return StaticStringsMethods.LEAGUE_ENGLAND;
        }
    }
    //======================================================
    public static int getLeagueIcon(String name) {
        switch (name){
            case PREMIER_LEAGUE:
                return R.drawable.ic_english_flag;
            case LIGUE_1 :
                return R.drawable.ic_french_flag;
            case SERIA_A :
                return R.drawable.ic_italian_flag;
            case BUNDES_LIGA :
                return R.drawable.ic_german_flag;
            case LA_LIGA :
                return R.drawable.ic_spanish_flag;
            default:
                return R.drawable.ic_english_flag;
        }
    }
    //======================================================
    public static ArrayList<Long> ScheduledDateDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        ArrayList<Long> returns = new ArrayList<>();
        returns.add(elapsedHours);
        returns.add(elapsedMinutes);
        returns.add(elapsedSeconds);
        return returns;
    }
    //======================================================
}
