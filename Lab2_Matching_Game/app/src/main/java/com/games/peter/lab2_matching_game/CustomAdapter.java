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

    }


    public CustomAdapter(Context context, ArrayList<User> items) {
        super(context, R.layout.userview, items);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        User usr = getItem(position);
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.userview, parent, false);
            vh.username = (TextView) view.findViewById(R.id.tv_username);
            vh.score = (TextView) view.findViewById(R.id.tv_score);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.username.setText(usr.getUsername());
        vh.score.setText(usr.getScore());
        return view;
    }

}