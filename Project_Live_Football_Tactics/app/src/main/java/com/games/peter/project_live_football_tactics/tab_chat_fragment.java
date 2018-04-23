package com.games.peter.project_live_football_tactics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Peter on 19/4/2018.
 */

public class tab_chat_fragment extends Fragment implements View.OnClickListener {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private Button btn_send_message;
    private EditText et_message_box;
    ArrayList<Message> messages=new ArrayList<>();
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View time, View view, int position) {
            if (time.getVisibility()==View.VISIBLE)
                time.setVisibility(View.INVISIBLE);
            else
                time.setVisibility(View.VISIBLE);
        }
    };
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_chat_fragment,container,false);
        btn_send_message=view.findViewById(R.id.button_chatbox_send);
        et_message_box=view.findViewById(R.id.edittext_chatbox);
        btn_send_message.setOnClickListener(this);

        messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Hello There"));
        messages.add(new Message(new User("2","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Hello man , how are you?"));
        messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"I am fine ,and you?"));
        messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Listen,I need a favor"));
        messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Do you know someone who supports Barcelona ?"));
        messages.add(new Message(new User("2","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"I am Fine too. Thanks"));
        messages.add(new Message(new User("2","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Yes I doo"));
        messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),"Great !! :D"));

        mMessageRecycler =  view.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(view.getContext(), messages, onItemClickCallback);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom!=mMessageAdapter.getItemCount()-1){
                    mMessageRecycler.getLayoutManager().smoothScrollToPosition(mMessageRecycler, null, mMessageAdapter.getItemCount() - 1);
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_chatbox_send){
            if (et_message_box.getText().toString().trim().isEmpty()){
                Toast.makeText(this.getContext(),"Cannot send empty message",Toast.LENGTH_SHORT).show();
                et_message_box.setText("");
                return;
            }
            else{
                messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),et_message_box.getText().toString().trim()));
                mMessageAdapter.notifyDataSetChanged();
                et_message_box.setText("");
                if (!mMessageRecycler.canScrollVertically(1)||mMessageRecycler.canScrollVertically(1))
                {
                    mMessageRecycler.getLayoutManager().smoothScrollToPosition(mMessageRecycler, null, mMessageAdapter.getItemCount() - 1);
                }
            }
        }
    }
}
