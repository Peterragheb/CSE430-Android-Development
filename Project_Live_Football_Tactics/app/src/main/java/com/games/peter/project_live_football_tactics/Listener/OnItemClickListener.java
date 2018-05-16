package com.games.peter.project_live_football_tactics.Listener;

import android.view.View;

/**
 * Created by Peter on 19/4/2018.
 */

public class OnItemClickListener implements View.OnClickListener {
    private int position;
    private View time;
    private OnItemClickCallback onItemClickCallback;

    public OnItemClickListener(View time,int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.time=time;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(time,view, position);
    }

    public interface OnItemClickCallback {
        void onItemClicked(View time,View view, int position);
    }
}