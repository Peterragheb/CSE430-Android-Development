package com.games.peter.fragment_test;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Peter on 17/3/2018.
 */

public class topFragment extends Fragment {
    EditText et_top_text;
    Button btn_send;
    FragmentListener fragmentListener;
    public interface FragmentListener{
        public void sendtext(String text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener=(FragmentListener) activity;

        }
        catch (ClassCastException ex){
            throw new ClassCastException(activity.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_fragment,container,false);
        et_top_text=view.findViewById(R.id.et_top_text);
        btn_send=view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentListener.sendtext(et_top_text.getText().toString());
            }
        });
        return view;
    }


}
