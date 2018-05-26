package com.games.peter.swipetorefreshtest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    SwipeRefreshLayout sr_layout;
    ListView lv_list;
    dbadapter myDb;
    ArrayList<user> users;
    customAdapter customAdapter;
    EditText et_name , et_age;
    Button btn_add;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        lv_list = findViewById(R.id.lv_list);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        btn_add = findViewById(R.id.btn_add);
        cursor = myDb.getAllRows();
        displayRecordSet(cursor);
        customAdapter = new customAdapter(this, R.layout.list_item, users);
        lv_list.setAdapter(customAdapter);
        sr_layout = findViewById(R.id.sr_layout);
        sr_layout.setOnRefreshListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        sr_layout.setRefreshing(true);
        cursor = myDb.getAllRows();
        displayRecordSet(cursor);
        customAdapter.clear();
        customAdapter.addAll(users);
        customAdapter.notifyDataSetChanged();
        sr_layout.setRefreshing(false);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    private void openDB() {
        myDb = new dbadapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }


    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";

        ArrayList<user> tempusers=new ArrayList<>();
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(dbadapter.COL_ROWID);
                String name = cursor.getString(dbadapter.COL_NAME);
                int age = cursor.getInt(dbadapter.COL_AGE);

                // Append data to the message:
                message += "id=" + id
                        +", name=" + name
                        +", age=" + age
                        +"\n";
                Log.v("new_user", message);
                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                tempusers.add(new user(name,age));
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


        // [TO_DO_B7]
        // Update the list view
        users = tempusers;

        // [TO_DO_B8]
        // Display a Toast message
        Toast.makeText(this,"Refreshed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_add.getId()){
            if (!et_name.getText().toString().isEmpty() && !et_age.getText().toString().isEmpty()){
                String name = et_name.getText().toString();
                int age = Integer.valueOf(et_age.getText().toString());
                myDb.insertRow(name,age);
                et_name.setText("");
                et_age.setText("");
            }


        }
    }
}
