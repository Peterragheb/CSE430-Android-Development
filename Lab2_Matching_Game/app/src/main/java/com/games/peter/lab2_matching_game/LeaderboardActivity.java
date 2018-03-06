package com.games.peter.lab2_matching_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    ListView listView ;
    DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        dbHandler=new DatabaseHandler(this);
        listView = (ListView) findViewById(R.id.list);
        ArrayList<User> users=dbHandler.getAllUsers();
        // Defined Array values to show in ListView
        String[] values = new String[] {
                users.get(0).getUsername()
        };
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        CustomAdapter adapter = new CustomAdapter(this, users);


        // Assign adapter to ListView
        listView.setAdapter(adapter);
    }
}
