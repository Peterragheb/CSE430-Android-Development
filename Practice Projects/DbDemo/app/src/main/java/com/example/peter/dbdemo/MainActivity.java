package com.example.peter.dbdemo;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et_username,et_score,et_date;
    Button btn_add,btn_display;
    dbadapter mydb;
    private static final String TAG = "DBAdapter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username=(EditText)findViewById(R.id.et_username);
        et_score=(EditText)findViewById(R.id.et_score);
        et_date=(EditText)findViewById(R.id.et_date);
        btn_add=(Button)findViewById(R.id.btn_add_record);
        btn_display=(Button)findViewById(R.id.btn_display_record);
        openDB();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_username.getText().toString().isEmpty()&&
                        !et_score.getText().toString().isEmpty()&&
                        !et_date.getText().toString().isEmpty()){
                    String username=et_username.getText().toString();
                    int score=Integer.parseInt(et_score.getText().toString());
                    String date=et_date.getText().toString();
                    long indx=mydb.insertRow(username,score,date);
                    Toast.makeText(getBaseContext(),"**Record "+indx,Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getBaseContext(),"Cannot Add Record",Toast.LENGTH_SHORT).show();


            }
        });
        btn_display.setOnClickListener(new View.OnClickListener() {
            Cursor cursor=mydb.getAllRows();
            @Override
            public void onClick(View view) {
                displayRecordSet(mydb.getAllRows());
            }
        });
    }
    private void openDB(){
        mydb=new dbadapter(this);
        mydb.open();
    }
    private void closeDB(){
        mydb.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }


    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(mydb.COL_ROWID);
                String username = cursor.getString(mydb.COL_USERNAME);
                int score = cursor.getInt(mydb.COL_SCORE);
                String date = cursor.getString(mydb.COL_DATE);

                // Append data to the message:
                message = "id=" + id
                        +", username=" + username
                        +", score=" + score
                        +", date=" + date
                        +"\n";
                Log.d(TAG,message);
                Log.d(TAG,"---------------------");

                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


        // [TO_DO_B7]
        // Update the list view

        // [TO_DO_B8]
        // Display a Toast message
    }
}
