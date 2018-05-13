package com.games.peter.project_live_football_tactics.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;

public class CustomMatchView extends LinearLayout implements Match.ChangeListener{
    private String fixtureid;
    //======================================================
    public CustomMatchView(Context context) {
        super(context);
    }
    //======================================================
    public CustomMatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //======================================================
    public CustomMatchView(Context context, AttributeSet attrs, int defStyle) {
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
        else if (field.equals("fixture_status_short")){ // changed         else if (field.equals("setFixture_status_short")){ to this
            if ((value.equals(StaticStringsMethods.HALF_TIME))||
                    (value.equals(StaticStringsMethods.FINISHED))){
                ImageView iv_minute = findViewById(R.id.iv_match_time_circle);
                TextView tv_time = findViewById(R.id.tv_match_time);
                tv_time.setText(value);
                iv_minute.setVisibility(View.INVISIBLE);
                tv_time.setTextColor(Color.BLACK);
            }
            //ADDED 10/5 TEST TODO
            else if (value.equals(StaticStringsMethods.STARTED)){
                TextView tv = findViewById(R.id.tv_match_home_score);
                tv.setText(value);
                TextView tv2 = findViewById(R.id.tv_match_home_score);
                tv2.setText(value);
            }
        }
        //======================================================
        else if (field.equals("id_team_season_away")){ }
        //======================================================
        else if (field.equals("id_team_season_home")){ }
        //======================================================
        else if (field.equals("number_goal_team_away")){
            TextView tv = findViewById(R.id.tv_match_away_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("number_goal_team_home")){
            TextView tv = findViewById(R.id.tv_match_home_score);
            tv.setText(value);
        }
        //======================================================
        else if (field.equals("schedule_date")){ }
        else if (field.equals("team_season_away_name")){ }
        else if (field.equals("team_season_home_name")){ }
        else if (field.equals("lineup_confirmed")){
            //trigger firebase notification if favorite team
            //dont know how to check if favorite team
        }
        //======================================================
        else if (field.equals("elapsed")){
            TextView tv_time = findViewById(R.id.tv_match_time);
            ImageView iv_minute = findViewById(R.id.iv_match_time_circle);
            if (iv_minute.getVisibility()!=VISIBLE)
                iv_minute.setVisibility(View.VISIBLE);
            if (tv_time.getCurrentTextColor()!=Color.WHITE)
                tv_time.setTextColor(Color.WHITE);
            tv_time.setText(value+"'");
        }
        //======================================================
    }

    public String getFixtureid() {
        return fixtureid;
    }
    //======================================================
    public void setFixtureid(String fixtureid) {
        this.fixtureid = fixtureid;
    }
    //======================================================
}
