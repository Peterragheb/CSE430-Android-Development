package com.games.peter.project_live_football_tactics.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Class.Match;
import com.games.peter.project_live_football_tactics.Class.StaticStringsMethods;
import com.games.peter.project_live_football_tactics.R;
import com.games.peter.project_live_football_tactics.View.CustomMatchView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class CustomMatchListAdapter extends ArrayAdapter<Match> {
    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        ImageView iv_minute;
        TextView time;
        TextView home_score;
        TextView away_score;
        TextView home_team;
        TextView away_team;
    }

    //=====================================

    //to specify the view type , inflater,and list of users
    private final int VIEW_TYPE_USER = 0;
    private final int VIEW_TYPE_OTHERS = 1;
    private LayoutInflater inflater;
    private ArrayList<Match> matches;


    //=====================================

    //constructor
    public CustomMatchListAdapter(Context context, int resource, ArrayList<Match> items) {
        //Todo change layout here
        super(context,resource, items);
        inflater = LayoutInflater.from(context);
        this.matches = items;
    }

    //=====================================

    //get count of users
    public int getCount() {
        return matches.size();
    }

    //=====================================

    //get the user view type
    public int getItemViewType(int position){
        return (position==0)? VIEW_TYPE_USER : VIEW_TYPE_OTHERS;
    }

    //=====================================


    public int getViewTypeCount(){
        return 2;
    }

    //=====================================

    //get the user at a certain position
    public Match getItem(int position) {
        return matches.get(position);
    }

    //=====================================

    //get user position
    public long getItemId(int position) {
        return position;
    }

    //=====================================

    //create view
    @Override
    public CustomMatchView getView(int position, View view, ViewGroup parent) {
        Match match = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
            view = (CustomMatchView) inflater.inflate(R.layout.match_row_details, parent, false);
            vh.time =  view.findViewById(R.id.tv_match_time);
            vh.home_team =  view.findViewById(R.id.tv_match_home_team);
            vh.away_team =  view.findViewById(R.id.tv_match_away_team);
            vh.home_score =  view.findViewById(R.id.tv_match_home_score);
            vh.away_score =  view.findViewById(R.id.tv_match_away_score);
            vh.iv_minute = view.findViewById(R.id.iv_match_time_circle);
            //=====================================
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================
        if ((match.getFixture_status_short().equals(StaticStringsMethods.HALF_TIME))||
                (match.getFixture_status_short().equals(StaticStringsMethods.FINISHED))){
            Log.v("Status","HT | FIN");
            vh.time.setText(match.getFixture_status_short());
            vh.iv_minute.setVisibility(View.INVISIBLE);
            vh.time.setTextColor(Color.BLACK);
            vh.home_score.setText(match.getNumber_goal_team_home()+"");
            vh.away_score.setText(match.getNumber_goal_team_away()+"");
        }
        else if ((match.getFixture_status_short().equals(StaticStringsMethods.NOT_STARTED))){
            String time = null;
            try {
                Log.v("Status","NS");
                SimpleDateFormat serversdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                serversdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat localsdf =new SimpleDateFormat("hh:mm a");
                localsdf.setTimeZone(TimeZone.getDefault());
                time = localsdf.format(serversdf.parse(match.getSchedule_date()));
                time=time.replace(" ","\n");
                vh.time.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            vh.iv_minute.setVisibility(View.INVISIBLE);
            vh.time.setTextColor(Color.BLACK);
            vh.home_score.setText("");
            vh.away_score.setText("");
        }
        else{
            Log.v("Status","ST");
            vh.time.setText(match.getElapsed()+"'");
            vh.iv_minute.setVisibility(View.VISIBLE);
            vh.home_score.setText(match.getNumber_goal_team_home()+"");
            vh.away_score.setText(match.getNumber_goal_team_away()+"");
            vh.time.setTextColor(Color.WHITE);
        }

        vh.home_team.setText(match.getTeam_season_home_name());
        vh.away_team.setText(match.getTeam_season_away_name());


        return (CustomMatchView) view;
    }

    //=====================================

}

