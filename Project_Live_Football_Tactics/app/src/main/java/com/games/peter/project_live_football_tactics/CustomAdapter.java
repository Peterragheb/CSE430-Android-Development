package com.games.peter.project_live_football_tactics;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Peter on 18/4/2018.
 */

public class CustomAdapter extends ArrayAdapter<Match> {
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
        public CustomAdapter(Context context, int resource, ArrayList<Match> items) {
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
        public View getView(int position, View view, ViewGroup parent) {
            Match match = getItem(position);
            ViewHolder vh;
            //=====================================

            if (view == null) {
                vh = new ViewHolder();
                inflater = LayoutInflater.from(getContext());
                //=====================================
                view = inflater.inflate(R.layout.match_row_details, parent, false);
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
            if (match.isStarted()){
                vh.time.setText(match.getMinute()+"'");
                vh.iv_minute.setVisibility(View.VISIBLE);
                vh.home_score.setText(match.getHome_score()+"");
                vh.away_score.setText(match.getAway_score()+"");
                vh.time.setTextColor(Color.WHITE);
            }

            else{
                String time = new SimpleDateFormat("hh:mm a").format(match.getStarttime());
                time=time.replace(" ","\n");
                vh.time.setText(time);
                vh.iv_minute.setVisibility(View.INVISIBLE);
                vh.time.setTextColor(Color.BLACK);
                vh.home_score.setText("");
                vh.away_score.setText("");
            }

            vh.home_team.setText(match.getHome_team());
            vh.away_team.setText(match.getAway_team());


            return view;
        }

        //=====================================

 }

