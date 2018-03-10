package com.games.peter.lab2_matching_game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by peter on 3/6/18.
 */
public class CustomAdapter extends ArrayAdapter<User> {

    private static class ViewHolder {
        TextView username;
        TextView score;
        TextView date;
    }
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private LayoutInflater inflater;
    private ArrayList<User> users;

    public CustomAdapter(Context context, ArrayList<User> items) {
        //Todo change layout here
        super(context, R.layout.userview, items);
        inflater = LayoutInflater.from(context);
        this.users = items;
    }
    public int getCount() {
        return users.size();
    }
    public int getItemViewType(int position){
        return (position==0)? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }
    public int getViewTypeCount(){
        return 2;
    }
    public User getItem(int position) {
        return users.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        User usr = getItem(position);
        ViewHolder vh;
        if (view == null) {

            if (position==0){
                vh = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.top_score, parent, false);
                vh.username = (TextView) view.findViewById(R.id.tv_username);
                vh.score = (TextView) view.findViewById(R.id.tv_score);
                vh.date = (TextView) view.findViewById(R.id.tv_date);
                view.setTag(vh);
            }
           else {
                vh = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.score_row, parent, false);
                vh.username = (TextView) view.findViewById(R.id.tv_username);
                vh.score = (TextView) view.findViewById(R.id.tv_score);
                vh.date = (TextView) view.findViewById(R.id.tv_date);
                view.setTag(vh);
            }

        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.username.setText(usr.getUsername());
        vh.score.setText(usr.getScore()+" "+usr.getScore_type());
        vh.date.setText(usr.getDate());
        return view;
    }

}