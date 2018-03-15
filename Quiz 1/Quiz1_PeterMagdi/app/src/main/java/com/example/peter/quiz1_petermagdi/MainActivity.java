package com.example.peter.quiz1_petermagdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv_list;
    public static final String COURSE_NAME_MESSAGE="course_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_list=(ListView)findViewById(R.id.list);
        String [] list_data={	"CSE115: Digital Design",
                "CSE125: Computer Programming (1)",
                "CSE127: Data Structures and Algorithms",
                "CSE128: Software Engineering (1)",
                "CSE215: Electronic Design Automation",
                "CSE221: Object-Oriented Analysis and Design",
                "CSE222: Software Engineering (2)",
                "CSE223: Operating Systems",
                "CSE224: Design and Analysis of Algorithms",
                "CSE225: Software Testing, Validation, and Verification"};

        final ArrayList<String> list_data2=new ArrayList<>();
        list_data2.add("CSE115: Digital Design");
        list_data2.add("CSE125: Computer Programming (1)");
        list_data2.add("CSE127: Data Structures and Algorithms");
        list_data2.add("CSE128: Software Engineering (1)");

        list_data2.add("CSE215: Electronic Design Automation");
        list_data2.add("CSE221: Object-Oriented Analysis and Design");
        list_data2.add("CSE222: Software Engineering (2)");
        list_data2.add("CSE223: Operating Systems");
        list_data2.add("CSE224: Design and Analysis of Algorithms");
        list_data2.add("CSE225: Software Testing, Validation, and Verification");

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list_data);
        lv_list.setAdapter(arrayAdapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name=list_data2.get(i);
                Intent intent=new Intent(MainActivity.this,CourseActivity.class);
                Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                intent.putExtra(COURSE_NAME_MESSAGE,name);
                startActivity(intent);

            }
        });
    }
}
