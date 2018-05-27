package com.games.peter.multiplefragmenttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Fragment_List extends Fragment {
    public ListView lv_list;
    public final static String [] list_data={	"CSE115: Digital Design",
            "CSE125: Computer Programming (1)",
            "CSE127: Data Structures and Algorithms",
            "CSE128: Software Engineering (1)",
            "CSE215: Electronic Design Automation",
            "CSE221: Object-Oriented Analysis and Design",
            "CSE222: Software Engineering (2)",
            "CSE223: Operating Systems",
            "CSE224: Design and Analysis of Algorithms",
            "CSE225: Software Testing, Validation, and Verification"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,null);
        lv_list = v.findViewById(R.id.lv_list);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1,list_data);
        lv_list.setAdapter(arrayAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey("SameActivity")){
            if (bundle.getBoolean("SameActivity")){
                Log.v("SAME_Activity_LISTENER","TRUE");
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name=list_data[position];
                        getActivity().getIntent().putExtra("title", name);
                        getActivity().getIntent().putExtra("description", name + " This is a test for fragment description. This is only a dummy date to be written on the screen to test the output being displayed on the screen of each smart phone the user can be using.");
                        Log.v("DETAILS","NAME :     "+name);
                        if (((MainActivity)getActivity()).fragment_details!=null){
                            Log.v("NULL_CHECK","NOT NULL");
                            ((MainActivity)getActivity()).fragment_details.update_values();
                        }

                    }
                });
            }
            else {
                lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name=list_data[position];
                        Log.v("DETAILS","NAME :     "+name);
                        Intent intent = new Intent(getActivity(),DetailsActivity.class);
                        intent.putExtra("title", name);
                        intent.putExtra("description", name + " This is a test for fragment description. This is only a dummy date to be written on the screen to test the output being displayed on the screen of each smart phone the user can be using.");
                        startActivity(intent);
                    }
                });
            }
        }else {
            lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name=list_data[position];
                    Intent intent = new Intent(getActivity(),DetailsActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("description", name + " This is a test for fragment description. This is only a dummy date to be written on the screen to test the output being displayed on the screen of each smart phone the user can be using.");
                    startActivity(intent);
                }
            });
        }
    }
}
