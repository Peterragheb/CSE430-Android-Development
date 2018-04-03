package com.example.peter.lab5_pedometer.Classes;

/**
 * Created by Peter on 30/3/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.peter.lab5_pedometer.R;

import java.util.ArrayList;


/**
 * Created by peter on 3/6/18.
 */
public class CustomAdapter extends ArrayAdapter<Record> {

    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        TextView date;
        TextView steps;
        TextView calories;
        TextView distance;
        TextView speed;
    }

    //=====================================

    //to specify the view type , inflater,and list of users
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private LayoutInflater inflater;
    private ArrayList<Record> records;


    //=====================================

    //constructor
    public CustomAdapter(Context context, int resource, ArrayList<Record> items) {
        //Todo change layout here
        super(context,resource, items);
        inflater = LayoutInflater.from(context);
        this.records = items;
    }

    //=====================================

    //get count of users
    public int getCount() {
        return records.size();
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
    public Record getItem(int position) {
        return records.get(position);
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
        Record record = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
                view = inflater.inflate(R.layout.record_row, parent, false);
            //=====================================
            vh.date =  view.findViewById(R.id.tv_record_date);
            vh.steps = view.findViewById(R.id.tv_record_steps);
            vh.calories =  view.findViewById(R.id.tv_record_calories);
            vh.distance =  view.findViewById(R.id.tv_record_distance);
            vh.speed =  view.findViewById(R.id.tv_record_speed);
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.date.setText(record.parseDate(null));
        vh.steps.setText(record.getSteps()+"");
        vh.calories.setText(record.getCalories()+"");
        vh.distance.setText(record.getDistance()+"");
        vh.speed.setText(record.getSpeed()+"");
        return view;
    }

    //=====================================


}