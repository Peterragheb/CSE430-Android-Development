package com.games.peter.project_live_football_tactics.Class;

import android.util.Log;

import com.games.peter.project_live_football_tactics.Adapter.ApiAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MatchListUpdater implements Runnable {
    ArrayList<Match> al_list1_matches;
    ArrayList<Match> al_list2_matches;
    ArrayList<Match> al_list3_matches;
    ArrayList<Match> al_list4_matches;
    ArrayList<Match> al_list5_matches;
    private boolean fnished;

    public MatchListUpdater(ArrayList<Match>al_list1_matches,ArrayList<Match>al_list2_matches,ArrayList<Match>al_list3_matches,ArrayList<Match>al_list4_matches,ArrayList<Match>al_list5_matches) {
        this.al_list1_matches = (ArrayList<Match>)al_list1_matches.clone();
        this.al_list2_matches = (ArrayList<Match>)al_list2_matches.clone();
        this.al_list3_matches = (ArrayList<Match>)al_list3_matches.clone();
        this.al_list4_matches = (ArrayList<Match>)al_list4_matches.clone();
        this.al_list5_matches = (ArrayList<Match>)al_list5_matches.clone();
    }
    //======================================================
    public ArrayList<Match> getAl_list1_matches(){
        return al_list1_matches;
    }
    //======================================================
    public ArrayList<Match> getAl_list2_matches() {
        return al_list2_matches;
    }
    //======================================================
    public ArrayList<Match> getAl_list3_matches() {
        return al_list3_matches;
    }
    //======================================================
    public ArrayList<Match> getAl_list4_matches() {
        return al_list4_matches;
    }
    //======================================================
    public ArrayList<Match> getAl_list5_matches() {
        return al_list5_matches;
    }
    //======================================================
    public boolean isFnished() {
        return fnished;
    }
    //======================================================
    public void setFnished(boolean fnished) {
        this.fnished = fnished;
    }
    //======================================================
    @Override
    public void run() {
        ArrayList<Match> al_list1,al_list2,al_list3,al_list4,al_list5;
        al_list1 = getMatchestoUpdate_List(al_list1_matches);
        al_list2 = getMatchestoUpdate_List(al_list2_matches);
        al_list3 = getMatchestoUpdate_List(al_list3_matches);
        al_list4 = getMatchestoUpdate_List(al_list4_matches);
        al_list5 = getMatchestoUpdate_List(al_list5_matches);

        updateMatchList(al_list1_matches,al_list1);
        updateMatchList(al_list2_matches,al_list2);
        updateMatchList(al_list3_matches,al_list3);
        updateMatchList(al_list4_matches,al_list4);
        updateMatchList(al_list5_matches,al_list5);
        synchronized (this) {
            fnished = true;
            notifyAll();
        }
    }
    //======================================================
    private ArrayList<Match> getMatchestoUpdate_List(ArrayList<Match> match_list){
        ArrayList<Match> list=new ArrayList<>();
        Calendar calendar =Calendar.getInstance();
        //TODO remove this
        //calendar.add(Calendar.DATE, -4);
        Date currentDate = calendar.getTime();
        Date matchDate;
        SimpleDateFormat serversdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        serversdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat localsdf =new SimpleDateFormat("hh:mm a");
        localsdf.setTimeZone(TimeZone.getDefault());
        Log.v("timeDiff","Current Time :"+localsdf.format(currentDate));
        for (int i=0;i<match_list.size();i++){
            try {
                matchDate = serversdf.parse(match_list.get(i).getSchedule_date());
                Log.v("timeDiff","Match Time :"+localsdf.format(matchDate));
                ArrayList<Long> difference =StaticStringsMethods.ScheduledDateDifference(currentDate,matchDate);
                if (difference.get(0)<1){
                    if (!match_list.get(i).getFixture_status_short().equals(StaticStringsMethods.FINISHED)){
                        //if (match_list.get(i).getFixture_status_short().equals(StaticStringsMethods.FINISHED)){
                            //TODO REMOVE
                            Log.v("getMatchestoUpdate_List","list size     ->"+match_list.size()+"  match   ->"+i);
                        list.add(match_list.get(i));
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    //======================================================
    private void updateMatchList(ArrayList<Match> original_match_list,ArrayList<Match> match_list){
        Log.v("updateMatchList","triggered!!!!!!!!!!!!!!!!!!!");

        for (int i=0; i<match_list.size();i++){
            String fixture_id = match_list.get(i).getFixture_id();
            int original_index = findIndex_ofFixtureInOriginalList(original_match_list,fixture_id);
            Match match = updateMatchDetails(match_list.get(i));
            if (match!=null){
                Log.v("updateMatchList","match not null");
                match_list.set(i,match);
            }

            if (original_index!=-1){
                Log.v("updateMatchList","index not null");
                original_match_list.set(original_index , match_list.get(i));
            }

        }
    }
    //======================================================
    private Match getMatch_Update(String fixture_id){
        return ApiAdapter.getFixtureUpdate(StaticStringsMethods.REQUEST_IN_FIXTURES+
                fixture_id+
                "?"+
                StaticStringsMethods.ACCESS_TOKEN_FIELD);
    }
    //======================================================
    private Match updateMatchDetails(Match match){
        return getMatch_Update(match.getFixture_id());
    }
    //======================================================
    private int findIndex_ofFixtureInOriginalList(ArrayList<Match> original_match_list,String fixture_id){
        for (int i=0 ;i<original_match_list.size();i++){
            if (original_match_list.get(i).getFixture_id().equals(fixture_id)) {
                return i;
            }
        }
        return -1;
    }
    //======================================================

}
