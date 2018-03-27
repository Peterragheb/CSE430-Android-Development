package com.games.peter.lab4_voice_calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Peter on 24/3/2018.
 */

public class CustomAdapter extends ArrayAdapter<Event> {

    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        TextView name;
        TextView date;
        TextView time;
    }

    //=====================================

    //to specify the view type , inflater,and list of users
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private LayoutInflater inflater;
    private ArrayList<Event> events;


    //=====================================

    //constructor
    public CustomAdapter(Context context, int resource, ArrayList<Event> items) {
        //Todo change layout here
        super(context,resource, items);
        inflater = LayoutInflater.from(context);
        this.events = items;
    }

    //=====================================

    //get count of users
    public int getCount() {
        return events.size();
    }

    //=====================================

    //get the user view type
    public int getItemViewType(int position){
        return (position==0)? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    //=====================================


    public int getViewTypeCount(){
        return 2;
    }

    //=====================================

    //get the user at a certain position
    public Event getItem(int position) {
        return events.get(position);
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
        Event event = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
                view = inflater.inflate(R.layout.list_item, parent, false);
            //=====================================
            vh.name = (TextView) view.findViewById(R.id.tv_event_name);
            vh.date = (TextView) view.findViewById(R.id.tv_event_date);
            vh.time = (TextView) view.findViewById(R.id.tv_event_time);
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.name.setText(event.getName());
        vh.date.setText(event.getDate());
        vh.time.setText(event.getTime());
        return view;
    }

    //=====================================


}