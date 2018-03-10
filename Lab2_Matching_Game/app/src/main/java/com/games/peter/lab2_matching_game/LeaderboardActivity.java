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
        //=====================================
        //assign variables
        listView = (ListView) findViewById(R.id.list);
        //=====================================
        //open database
        openDB();

        //=====================================
        //display all users
        displayRecordSet(mydb.getAllRows());

    }

    //open database
    private void openDB(){
        mydb=new DatabaseHandler(this);
        mydb.open();
    }


    //close database
    private void closeDB(){
        if (mydb!=null)
            mydb.close();
    }


    //on activity destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    //display all records
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
                //=====================================

                // Append data to the message:
                message = "id=" + id
                        +", username=" + username
                        +", score=" + score
                        +", score_type=" + score_type
                        +", date=" + date
                        +"\n";
                Log.d(TAG,message);
                Log.d(TAG,"---------------------");
                //=====================================

                //add users to arraylist
                users.add(new User(username,score,score_type,date));
            } while(cursor.moveToNext());
        }
        //=====================================

        // Close the cursor to avoid a resource leak.
        cursor.close();
        //=====================================
        //sort list of users according to the score
        Collections.sort(users);
        //=====================================
        //creating new custom adapter
        CustomAdapter adapter = new CustomAdapter(this, users);
        //binding adapter to the listview
        listView.setAdapter(adapter);
        //=====================================

    }

}
