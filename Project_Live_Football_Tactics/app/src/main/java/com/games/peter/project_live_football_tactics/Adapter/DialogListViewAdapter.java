package com.games.peter.project_live_football_tactics.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Class.DialogChoiceItem;
import com.games.peter.project_live_football_tactics.R;

import java.util.ArrayList;


public class DialogListViewAdapter extends ArrayAdapter<DialogChoiceItem> {
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
    private ArrayList<DialogChoiceItem> settingsItems;


    //=====================================

    //constructor
    public DialogListViewAdapter(Context context, int resource, ArrayList<DialogChoiceItem> items) {
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
    public DialogChoiceItem getItem(int position) {
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
        DialogChoiceItem DialogChoiceItem = getItem(position);
        ViewHolder vh;
        //=====================================

        if (view == null) {
            vh = new ViewHolder();
            inflater = LayoutInflater.from(getContext());
            //=====================================
            view = inflater.inflate(R.layout.signup_dialog_choice_item, parent, false);
            vh.iv_icon =  view.findViewById(R.id.iv_icon);
            vh.tv_title =  view.findViewById(R.id.tv_name);
            //=====================================
            view.setTag(vh);

        }
        //=====================================


        else {
            vh = (ViewHolder) view.getTag();
        }
        //=====================================

        vh.iv_icon.setImageResource(DialogChoiceItem.getImageResource());
        vh.tv_title.setText(DialogChoiceItem.getName());


        return view;
    }

    //=====================================

}

