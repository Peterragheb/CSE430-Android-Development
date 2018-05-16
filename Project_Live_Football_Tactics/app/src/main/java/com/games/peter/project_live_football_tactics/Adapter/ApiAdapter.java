package com.games.peter.project_live_football_tactics.Adapter;

import android.os.AsyncTask;
import android.util.Log;

import com.games.peter.project_live_football_tactics.Class.DialogChoiceItem;
import com.games.peter.project_live_football_tactics.Class.Lineup;
import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.Player;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public class ApiAdapter {
    //===============================================================================================================
    private static String access_token;
    private static JSONObject data = null;
    //===============================================================================================================
    public static void getJSON(final String ApiURL) throws ExecutionException, InterruptedException {
        try{


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                access_token = getAccessToken();
                if (access_token==null||access_token.equals(""))
                    return;
                   // this.cancel(true);
            }
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(ApiURL+access_token);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }
                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    Log.d("Json_Received",data.toString());

                }
            }
        }.execute().get();
        }catch (CancellationException e){
            Log.v("ERROR_GET_JSON",e.getMessage());
        }
    }
    //======================================================
    public static String getSeasonId(String ApiURL){
        try {
            getJSON(ApiURL);
            if (data!=null){
                return parseSeasonId(data.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    private static String parseSeasonId(String json){
        String ApiUrl = "https://api.sportdeer.com/v1/leagues/8/seasons?page=";
        boolean found = false;
        final String PAGINATION = "pagination";
        final String TOTAL = "total";
        final String DOCS = "docs";
        final String IS_LAST_SEASON = "is_last_season";
        final String ID = "_id";
        try {
            JSONObject SeasonsDataObject =new JSONObject(json);
            if (!SeasonsDataObject.isNull(PAGINATION)){
                JSONObject pagination = SeasonsDataObject.getJSONObject(PAGINATION);
                int total_no_of_seasons = Integer.valueOf(pagination.getString(TOTAL));
                JSONArray docs = SeasonsDataObject.getJSONArray(DOCS);
                JSONObject season = docs.getJSONObject(0);
                for (int i=0;i<docs.length();i++){
                    season = docs.getJSONObject(i);
                    if ((!season.isNull(IS_LAST_SEASON))&&season.getBoolean(IS_LAST_SEASON)==true){
                        found = true;
                        break;
                    }
                }
                if (found)
                    return season.getString(ID);

                for (int i=0;i<(total_no_of_seasons/docs.length())-1;i++){ //if total number of seasons is more that a page (30) then will call the api number of times to get next page
                    int j=i+1;
                    getJSON(ApiUrl+j+"&"+StaticStringsMethods.ACCESS_TOKEN_FIELD);
                    if (data!=null){
                        docs = SeasonsDataObject.getJSONArray(DOCS);
                        season = docs.getJSONObject(0);
                        for (int k=0;k<docs.length();k++){
                            season = docs.getJSONObject(k);
                            if ((!season.isNull(IS_LAST_SEASON))&&season.getBoolean(IS_LAST_SEASON)==true){
                                found = true;
                                break;
                            }
                        }
                        if (found)
                            return season.getString(ID);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    public static ArrayList<DialogChoiceItem> getSeasonTeams(int icon_resource,String ApiURL){
        try {
            getJSON(ApiURL);
            if (data!=null){
                return parseSeasonTeams(icon_resource,data.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    private static ArrayList<DialogChoiceItem> parseSeasonTeams(int icon_resource,String json){
        final String DOCS = "docs";
        final String TEAM_NAME = "team_name";
        final String TEAM_ID = "_id";
        ArrayList<DialogChoiceItem> teams = new ArrayList<>();
        try {
            JSONObject SeasonsDataObject =new JSONObject(json);
            JSONArray docs = SeasonsDataObject.getJSONArray(DOCS);
            JSONObject team;
            for (int i=0;i<docs.length();i++){
                team = docs.getJSONObject(i);
                if (team.has(TEAM_NAME)){
                    teams.add(new DialogChoiceItem(icon_resource,team.getString(TEAM_NAME),team.getInt(TEAM_ID)));
                }
            }
            return teams;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return teams;
    }
    //======================================================
    public static String getAccessToken(){
        final JSONObject[] jsonString = {null};
        final String REFRESH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1YWQ2MWI1OTM0OGFhNzA3MGQwNjg3MzYiLCJpYXQiOjE1MjQ1MjQxMTd9.YloIBVTLovSCP-OoHfgVEfgipCFra8_Yzo9nqmHd3K8";
        final String ACCESS_TOKEN_REQUEST_URL = "https://api.sportdeer.com/v1/accessToken?refresh_token=";
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL(ACCESS_TOKEN_REQUEST_URL+REFRESH_TOKEN);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        StringBuffer json = new StringBuffer(1024);
                        String tmp = "";

                        while((tmp = reader.readLine()) != null)
                            json.append(tmp).append("\n");
                        reader.close();

                        jsonString[0] = new JSONObject(json.toString());

                        if(jsonString[0].getInt("cod") != 200) {
                            System.out.println("Cancelled");
                            return null;
                        }
                    } catch (Exception e) {

                        System.out.println("Exception "+ e.getMessage());
                        return null;
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void Void) {
                    if(jsonString[0]!=null){
                        //Log.d("Json_Received",jsonString[0].toString());
                    }
                }
            }.execute().get();
            if (jsonString[0]!=null)
                return jsonString[0].getString("new_access_token");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    //======================================================
    public static ArrayList<Match> getFixtures(String ApiURL){
        try {
            Log.v("Fixtures_URL",ApiURL);
            getJSON(ApiURL);
            if (data!=null){
                return parseFixtures(data.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    private static ArrayList<Match> parseFixtures(String json){
        final String DOCS = "docs";
        final String fixture_id = "_id";
        final String id_season= "id_season";
        final String fixture_status_short = "fixture_status_short";
        final String id_team_season_away = "id_team_season_away";
        final String id_team_season_home = "id_team_season_home";
        final String number_goal_team_away ="number_goal_team_away";
        final String number_goal_team_home ="number_goal_team_home";
        final String scheduled_date ="schedule_date";
        final String team_season_away_name = "team_season_away_name";
        final String team_season_home_name = "team_season_home_name";
        final String lineup_confirmed= "lineup_confirmed";
        final String elapsed_time = "elapsed";
        ArrayList<Match> fixtures = new ArrayList<>();
        try {
            JSONObject FixturesDataObject =new JSONObject(json);
            JSONArray docs = FixturesDataObject.getJSONArray(DOCS);
            JSONObject fixture;
            for (int i=0;i<docs.length();i++){
                fixture = docs.getJSONObject(i);
                Log.v("fixture",fixture.toString());
                if (fixture.has(fixture_status_short)&&fixture.has(number_goal_team_away)&&fixture.has(number_goal_team_home)){
                    if(fixture.has(lineup_confirmed)){
                        if(fixture.has(elapsed_time)){
                            Log.v("fixtureadd","true");
                            fixtures.add(new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                    fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                    fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                    fixture.getString(team_season_home_name), fixture.getString(lineup_confirmed), fixture.getString(elapsed_time)));
                        } else
                        {
                            Log.v("fixtureadd","true");
                            fixtures.add(new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                    fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                    fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                    fixture.getString(team_season_home_name), fixture.getString(lineup_confirmed), StaticStringsMethods.NOT_STARTED));
                        }
                    }else
                    {
                        Log.v("fixtureadd","true");
                        fixtures.add(new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                fixture.getString(team_season_home_name), StaticStringsMethods.NO_LINEUP, StaticStringsMethods.NOT_STARTED));
                    }
                }
            }
            return fixtures;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fixtures;
    }
    //======================================================
    public static Match getFixtureUpdate(String ApiURL){
        try {
            Log.v("FixtureURL",ApiURL);
            getJSON(ApiURL);
            if (data!=null){
                Log.v("FixtureDATA",data.toString());
                return parseFixtureData(data.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    private static Match parseFixtureData(String json){
        final String DOCS = "docs";
        final String fixture_id = "_id";
        final String id_season= "id_season";
        final String fixture_status_short = "fixture_status_short";
        final String id_team_season_away = "id_team_season_away";
        final String id_team_season_home = "id_team_season_home";
        final String number_goal_team_away ="number_goal_team_away";
        final String number_goal_team_home ="number_goal_team_home";
        final String scheduled_date ="schedule_date";
        final String team_season_away_name = "team_season_away_name";
        final String team_season_home_name = "team_season_home_name";
        final String lineup_confirmed= "lineup_confirmed";
        final String elapsed_time = "elapsed";
        Match match = null;
        try {
            JSONObject FixturesDataObject =new JSONObject(json);
            JSONArray docs = FixturesDataObject.getJSONArray(DOCS);
            JSONObject fixture;
                fixture = docs.getJSONObject(0);
                Log.v("fixture",fixture.toString());
                if (fixture.has(fixture_status_short)&&fixture.has(number_goal_team_away)&&fixture.has(number_goal_team_home)){
                    if(fixture.has(lineup_confirmed)){
                        if(fixture.has(elapsed_time)){
                            Log.v("fixtureadd","true");
                            match = new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                    fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                    fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                    fixture.getString(team_season_home_name), fixture.getString(lineup_confirmed), fixture.getString(elapsed_time));
                        } else
                        {
                            Log.v("fixtureadd","true");
                            match = new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                    fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                    fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                    fixture.getString(team_season_home_name), fixture.getString(lineup_confirmed), StaticStringsMethods.NOT_STARTED);
                        }
                    }else
                    {
                        Log.v("fixtureadd","true");
                        match = new Match(fixture.getString(fixture_id), fixture.getString(id_season), fixture.getString(fixture_status_short),
                                fixture.getString(id_team_season_away), fixture.getString(id_team_season_home), fixture.getString(number_goal_team_away),
                                fixture.getString(number_goal_team_home), fixture.getString(scheduled_date), fixture.getString(team_season_away_name),
                                fixture.getString(team_season_home_name), StaticStringsMethods.NO_LINEUP, StaticStringsMethods.NOT_STARTED);
                    }
                }

            return match;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return match;
    }
    //======================================================
    public static ArrayList<Lineup> getFixtureLineup(String ApiURL){
        try {
            Log.v("LineupURL",ApiURL);
            getJSON(ApiURL);
            if (data!=null){
                Log.v("LineupsDATA",data.toString());
                return parseLineupData(data.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //======================================================
    private static ArrayList<Lineup> parseLineupData(String json){
        final String DOCS = "docs";
        final String PAGINATION = "pagination";
        final String PAGES = "pages";
        final String fixture_id = "id_fixture";
        final String team_season_id = "id_team_season";
        final String shirtnumber = "shirtNumber";
        final String row = "row";
        final String col = "col";
        final String player_name = "player_name";
        final String is_startingXI = "is_startingXI";
        ArrayList<Lineup> lineups = new ArrayList<>();
        try {
            JSONObject LineupsDataObject =new JSONObject(json);
            JSONArray docs = LineupsDataObject.getJSONArray(DOCS);
            Lineup home_team =new Lineup();
            Lineup away_team =new Lineup();
            JSONObject player = docs.getJSONObject(0);
            int away_team_id=-1,home_team_id=-1;
            for (int i=0;i<docs.length();i++){
                player = docs.getJSONObject(i);
                Log.v("lineup_player",player.toString());
                if (player.has(is_startingXI))
                {
                    if (player.getBoolean(is_startingXI)){
                        if (away_team_id ==-1){
                            away_team_id =player.getInt(team_season_id);
                        }
                        else if (player.getInt(team_season_id)!=away_team_id && away_team_id !=-1 && home_team_id ==-1){
                            home_team_id =player.getInt(team_season_id);
                        }
                        if (player.getInt(team_season_id)==away_team_id){
                            away_team.addPlayer(new Player(player.getInt(row),player.getInt(col),player.getString(player_name),player.getInt(shirtnumber),player.getInt(team_season_id)));
                        }
                        if (player.getInt(team_season_id)==home_team_id){
                            home_team.addPlayer(new Player(player.getInt(row),player.getInt(col),player.getString(player_name),player.getInt(shirtnumber),player.getInt(team_season_id)));
                        }
                    }
                }
            }
            if (!LineupsDataObject.isNull(PAGINATION)){
                JSONObject pagination = LineupsDataObject.getJSONObject(PAGINATION);
                int no_of_pages = pagination.getInt(PAGES);
                for (int i = 2;i<=no_of_pages;i++){//pages start by default from 1 so the first time we already queried for page 1
                    Log.v("SECOND_ROUND","Entered");
                    getJSON(StaticStringsMethods.REQUEST_IN_FIXTURES+
                            player.getInt(fixture_id)+
                            StaticStringsMethods.REQUEST_LINEUPS+
                            StaticStringsMethods.PAGE_FIELD+
                            i+
                            "&"+
                            StaticStringsMethods.ACCESS_TOKEN_FIELD);
                    if (data!=null){
                        Log.v("SECOND_ROUND_JSON",data.toString());
                        docs = data.getJSONArray(DOCS);
                        player = docs.getJSONObject(0);
                        for (int k=0;k<docs.length();k++){
                            player = docs.getJSONObject(k);
                            if (player.has(is_startingXI))
                            {
                                if (player.getBoolean(is_startingXI)){
                                    if (away_team_id ==-1){
                                        away_team_id =player.getInt(team_season_id);
                                    }
                                    else if (player.getInt(team_season_id)!=away_team_id && away_team_id !=-1 && home_team_id ==-1){
                                        home_team_id =player.getInt(team_season_id);
                                    }
                                    if (player.getInt(team_season_id)==away_team_id){
                                        Log.v("SECOND_ROUND_AWAY","true");
                                        away_team.addPlayer(new Player(player.getInt(row),player.getInt(col),player.getString(player_name),player.getInt(shirtnumber),player.getInt(team_season_id)));
                                    }
                                    if (player.getInt(team_season_id)==home_team_id){
                                        Log.v("SECOND_ROUND_HOME","true");
                                        home_team.addPlayer(new Player(player.getInt(row),player.getInt(col),player.getString(player_name),player.getInt(shirtnumber),player.getInt(team_season_id)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            lineups.add(away_team);
            lineups.add(home_team);
            return lineups;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return lineups;
    }
    //======================================================
}
