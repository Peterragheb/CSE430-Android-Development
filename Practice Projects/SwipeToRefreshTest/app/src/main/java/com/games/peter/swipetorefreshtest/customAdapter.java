package com.games.peter.swipetorefreshtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customAdapter extends ArrayAdapter<user> {

    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        TextView name;
        TextView age;
    }

    //=====================================

    //to specify the view type , inflater,and list of users
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private LayoutInflater inflater;
    private ArrayList<user> users;


    //=====================================

    //constructor
    public customAdapter(Context context, int resource, ArrayList<user> items) {
        //Todo change layout here
        super(context,resource, items);
        inflater = LayoutInflater.from(context);
        this.users = items;
    }

    //=====================================

    //get count of users
    public int getCount() {
        return users.size();
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
    public user getItem(int position) {
        return users.get(position);
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
        user user = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
            view = inflater.inflate(R.layout.list_item, parent, false);
            //=====================================
            vh.name = (TextView) view.findViewById(R.id.tv_name);
            vh.age = (TextView) view.findViewById(R.id.tv_age);
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.name.setText(user.getName());
        vh.age.setText(user.getAge()+"");
        return view;
    }

    //=====================================


}
