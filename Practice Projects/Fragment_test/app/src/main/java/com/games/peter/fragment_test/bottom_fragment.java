package com.games.peter.fragment_test;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Peter on 17/3/2018.
 */

public class bottom_fragment extends Fragment {
    TextView tv_text;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_fragment,container,false);
        tv_text=view.findViewById(R.id.tv_text);
        return view;
    }
    public void ApplyText(String text){
        tv_text.setText(text);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("text",tv_text.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            tv_text.setText(savedInstanceState.getString("text"));
        }
        super.onActivityCreated(savedInstanceState);
    }
}
