package com.games.peter.project_live_football_tactics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Class.SettingsItem;
import com.games.peter.project_live_football_tactics.R;

import java.util.ArrayList;

/**
 * Created by Peter on 26/4/2018.
 */


public class ListViewAdapter extends ArrayAdapter<SettingsItem> {
    //=====================================

    //create class for all the fields in a single row of the listview
    private static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
    }

    //=====================================

    //to specify the view type , inflater,and list of users
    private final int VIEW_TYPE_USER = 0;
    private final int VIEW_TYPE_OTHERS = 1;
    private LayoutInflater inflater;
    private ArrayList<SettingsItem> settingsItems;


    //=====================================

    //constructor
    public ListViewAdapter(Context context, int resource, ArrayList<SettingsItem> items) {
        //Todo change layout here
        super(context,resource, items);
        inflater = LayoutInflater.from(context);
        this.settingsItems = items;
    }

    //=====================================

    //get count of users
    public int getCount() {
        return settingsItems.size();
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
    public SettingsItem getItem(int position) {
        return settingsItems.get(position);
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
        SettingsItem settingsItem = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
            view = inflater.inflate(R.layout.settings_item, parent, false);
            vh.iv_icon =  view.findViewById(R.id.iv_setttings_item_icon);
            vh.tv_title =  view.findViewById(R.id.tv_settings_item_title);

            //=====================================
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.iv_icon.setImageResource(settingsItem.getImage_resource());
        vh.tv_title.setText(settingsItem.getTitle());


        return view;
    }

    //=====================================

}

