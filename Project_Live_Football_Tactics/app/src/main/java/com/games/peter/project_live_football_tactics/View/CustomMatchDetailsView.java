package com.games.peter.project_live_football_tactics.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;

public class CustomMatchDetailsView extends LinearLayout implements Match.ChangeListener{
    private String fixtureid;
    //======================================================
    public CustomMatchDetailsView(Context context) {
        super(context);
    }
    //======================================================
    public CustomMatchDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //======================================================
    public CustomMatchDetailsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //======================================================
    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
    }
    //======================================================
    @Override
    public void onChange(String field,String value) {
        Log.v("onChange","triggered!!!!!!!!!!!!!!!!!!!");
        if (field.equals("fixture_id")){ }
        //======================================================
        else if (field.equals("id_season")){ }
        //======================================================
        else if (field.equals("setFixture_status_short")){
            if ((value.equals(StaticStringsMethods.HALF_TIME))||
                    (value.equals(StaticStringsMethods.FINISHED))){
                TextView tv_time = findViewById(R.id.tv_match_fragment_time);
                tv_time.setText(value);
            }
        }
        //======================================================
        else if (field.equals("setId_team_season_away")){ }
        //======================================================
        else if (field.equals("setId_team_season_home")){ }
        //======================================================
        else if (field.equals("number_goal_team_away")){
            TextView tv = findViewById(R.id.tv_match_fragment_away_team_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("number_goal_team_home")){
            TextView tv = findViewById(R.id.tv_match_fragment_home_team_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("schedule_date")){ }
        else if (field.equals("team_season_away_name")){ }
        else if (field.equals("team_season_home_name")){ }
        else if (field.equals("lineup_confirmed")){
            //trigger firebase notification if favorite team
            //dont know how to check if favorite team
           //Activity myActivity = (AppCompatActivity)getContext();
            //tab_Lineup_fragment fragment = (tab_Lineup_fragment) ((MatchActivity)myActivity).getSupportFragmentManager().findFragmentById(((MatchActivity)myActivity).LINUP_TAB_ID);
            //fragment.getLineups();
        }
        //======================================================
        else if (field.equals("elapsed")){
            TextView tv_time = findViewById(R.id.tv_match_fragment_time);
            tv_time.setText(value+"'");
        }
        //======================================================
    }
    //======================================================
    public String getFixtureid() {
        return fixtureid;
    }
    //======================================================
    public void setFixtureid(String fixtureid) {
        this.fixtureid = fixtureid;
    }
    //======================================================
}