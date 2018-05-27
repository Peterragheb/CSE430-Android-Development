package com.games.peter.multiplefragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_Details extends Fragment {
    TextView title ,description;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details,null);
        title =v.findViewById(R.id.tv_title);
        description =v.findViewById(R.id.tv_description);
        if (getActivity().getIntent().getExtras()!=null){
            if (getActivity().getIntent().getExtras().containsKey("title") && getActivity().getIntent().getExtras().containsKey("description")){
                title.setText(getActivity().getIntent().getStringExtra("title"));
                description.setText(getActivity().getIntent().getStringExtra("description"));
            }
        }
        else{
            title.setText(Fragment_List.list_data[0]);
            description.setText(Fragment_List.list_data[0]+" his is a test for fragment description. This is only a dummy date to be written on the screen to test the output being displayed on the screen of each smart phone the user can be using.");
        }
        return v;
    }
    public void update_values(){
        if (getActivity().getIntent().getExtras()!=null){
            if (getActivity().getIntent().getExtras().containsKey("title") && getActivity().getIntent().getExtras().containsKey("description")){
                Log.v("UPDATING","TRUE");
                title.setText(getActivity().getIntent().getStringExtra("title"));
                description.setText(getActivity().getIntent().getStringExtra("description"));
            }
        }
    }
}
