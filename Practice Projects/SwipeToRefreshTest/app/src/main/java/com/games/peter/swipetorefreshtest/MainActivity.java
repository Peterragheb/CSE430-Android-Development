package com.games.peter.swipetorefreshtest;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout sr_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sr_layout = findViewById(R.id.sr_layout);
        sr_layout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        sr_layout.setRefreshing(true);
        Toast.makeText(this,"Refreshed",Toast.LENGTH_SHORT).show();
        sr_layout.setRefreshing(false);
    }
}
