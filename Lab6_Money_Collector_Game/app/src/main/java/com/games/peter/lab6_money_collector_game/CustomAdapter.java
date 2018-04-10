package com.games.peter.lab6_money_collector_game;

/**
 * Created by Peter on 30/3/2018.
 */

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
public class CustomAdapter extends ArrayAdapter<Record> {

    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        TextView date;
        TextView score;
        TextView coins;
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
                view = inflater.inflate(R.layout.list_item, parent, false);
            //=====================================
            vh.date =  view.findViewById(R.id.tv_record_date);
            vh.score = view.findViewById(R.id.tv_record_score);
            vh.coins =  view.findViewById(R.id.tv_record_coins);
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.date.setText(record.parseDate(null));
        vh.score.setText(record.getScore()+"");
        vh.coins.setText(record.getCoins()+"");
        return view;
    }

    //=====================================


}