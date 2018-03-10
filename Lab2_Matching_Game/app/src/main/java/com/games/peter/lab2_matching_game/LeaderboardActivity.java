package com.games.peter.lab2_matching_game;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {
    ListView listView ;
    DatabaseHandler mydb;
    private static final String TAG = "DBAdapter";
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        listView = (ListView) findViewById(R.id.list);
        openDB();
        displayRecordSet(mydb.getAllRows());
        // Assign adapter to ListView

    }
    private void openDB(){
        mydb=new DatabaseHandler(this);
        mydb.open();
    }
    private void closeDB(){
        if (mydb!=null)
            mydb.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";
        users=new ArrayList<>();
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(mydb.COL_ROWID);
                String username = cursor.getString(mydb.COL_USERNAME);
                int score = cursor.getInt(mydb.COL_SCORE);
                String score_type = cursor.getString(mydb.COL_SCORE_TYPE);
                String date = cursor.getString(mydb.COL_DATE);

                // Append data to the message:
                message = "id=" + id
                        +", username=" + username
                        +", score=" + score
                        +", score_type=" + score_type
                        +", date=" + date
                        +"\n";
                Log.d(TAG,message);
                Log.d(TAG,"---------------------");

                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                users.add(new User(username,score,score_type,date));
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();
        Collections.sort(users);
        CustomAdapter adapter = new CustomAdapter(this, users);

        // [TO_DO_B7]
        // Update the list view
        listView.setAdapter(adapter);
        // [TO_DO_B8]
        // Display a Toast message
    }

}
