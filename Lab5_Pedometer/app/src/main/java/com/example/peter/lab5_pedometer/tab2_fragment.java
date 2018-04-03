package com.example.peter.lab5_pedometer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.peter.lab5_pedometer.Classes.CustomAdapter;
import com.example.peter.lab5_pedometer.Classes.DBAdapter;
import com.example.peter.lab5_pedometer.Classes.Record;
import com.example.peter.lab5_pedometer.Classes.UiUpdateTrigger;

import java.util.ArrayList;

/**
 * Created by peter on 3/30/18.
 */

public class tab2_fragment extends Fragment implements UiUpdateTrigger.ChangeListener {
    //TODO MAKE BROADCAST WHEN DATA IN DATABASE CHANGE TO TRIGGER UPDATE TABLE
    private static final String TAG = "DBAdapter";
    ListView lv_records;
    DBAdapter dbAdapter;
    ArrayList<Record> records;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab2_fragment,container,false);
        lv_records=view.findViewById(R.id.lv_records);
        openDB();
        displayRecordSet();
        tab1_fragment.uiUpdateTrigger.setListener(this);
//        records.add(new Record(Calendar.getInstance().getTime(), 1200,500,1.2f,1.5f));
//        records.add(new Record(Calendar.getInstance().getTime(), 200,100,0.2f ,3.2f));
//        records.add(new Record(Calendar.getInstance().getTime(), 2200,700,2.2f ,1f));
//        records.add(new Record(Calendar.getInstance().getTime(), 5500,1500,3.2f, 5f));
//        CustomAdapter customAdapter=new CustomAdapter(view.getContext(),R.layout.record_row,records);
        //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(view.getContext(),R.layout.record_row,R.id.tv_record_date,dates);
        //lv_records.setAdapter(customAdapter);
        return view;
    }

    private void openDB(){
        dbAdapter=new DBAdapter(this.getContext());
        dbAdapter.open();
    }
    private void closeDB(){
        if (dbAdapter!=null)
            dbAdapter.close();
    }

    //display all records
    public void displayRecordSet() {
        Cursor cursor=dbAdapter.getAllRows();
        String message = "";
        records=new ArrayList<>();
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String date = cursor.getString(dbAdapter.COL_DATE);
                int steps = cursor.getInt(dbAdapter.COL_STEPS);
                float calories = cursor.getFloat(dbAdapter.COL_CALORIES);
                float distance = cursor.getFloat(dbAdapter.COL_DISTANCE);
                float speed= cursor.getFloat(dbAdapter.COL_SPEED);
                //=====================================

                // Append data to the message:
                message = "date=" + date
                        +", steps=" + steps
                        +", calories=" + calories
                        +", distance=" + distance
                        +", speed=" + speed
                        +"\n";
                Log.d(TAG,message);
                Log.d(TAG,"---------------------");
                //=====================================

                //add users to arraylist
                records.add(new Record(null,date,steps,calories,distance,speed));
            } while(cursor.moveToNext());
        }
        //=====================================

        // Close the cursor to avoid a resource leak.
        cursor.close();
        //=====================================
        //sort list of users according to the score
       // Collections.sort(records);
        //=====================================
        //creating new custom adapter
        //CustomAdapter adapter = new CustomAdapter(this, users);

        CustomAdapter adapter = new CustomAdapter(this.getContext(),R.layout.record_row,records);
        //binding adapter to the listview
        lv_records.setAdapter(adapter);
        //=====================================

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    @Override
    public void onChange() {
        displayRecordSet();
    }
}
