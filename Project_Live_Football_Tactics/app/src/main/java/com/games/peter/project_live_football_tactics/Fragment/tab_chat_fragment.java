package com.games.peter.project_live_football_tactics.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.games.peter.project_live_football_tactics.Adapter.MessageListAdapter;
import com.games.peter.project_live_football_tactics.Class.Message;
import com.games.peter.project_live_football_tactics.Class.User;
import com.games.peter.project_live_football_tactics.Listener.OnItemClickListener;
import com.games.peter.project_live_football_tactics.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Peter on 19/4/2018.
 */

public class tab_chat_fragment extends Fragment implements View.OnClickListener {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private Button btn_send_message;
    private EditText et_message_box;
    private int team_id;//favorite team id
    private String user_id;
    private String user_name;
    private String fixture_id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    Animation anim_scale_up,anim_scale_down;
    DatabaseReference chatRoom ;
    ArrayList<Message> messages=new ArrayList<>();
    //===============================================================================================================
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_chat_fragment,container,false);
        initComponents();
        initUIComponents(view);
        initChatRoom_Update();
        return view;
    }
    //======================================================
    private void initUIComponents(View view){
        btn_send_message=view.findViewById(R.id.button_chatbox_send);
        et_message_box=view.findViewById(R.id.edittext_chatbox);
        btn_send_message.setOnClickListener(this);
        anim_scale_up = AnimationUtils.loadAnimation(this.getContext(), R.anim.scale_up);
        anim_scale_down = AnimationUtils.loadAnimation(this.getContext(), R.anim.scale_down);
        mMessageRecycler =  view.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(view.getContext(), messages, onItemClickCallback);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);
        mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mMessageAdapter.getItemCount()>0){
                    if (bottom!=mMessageAdapter.getItemCount()-1){
                        mMessageRecycler.getLayoutManager().smoothScrollToPosition(mMessageRecycler, null, mMessageAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }
    //======================================================
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }
    //======================================================
    private void initChatRoom_Update(){
        if (getActivity().getIntent().hasExtra("fixture_id")&&getActivity().getIntent().hasExtra("team_id")&&getActivity().getIntent().hasExtra("user_id")){
            fixture_id = getActivity().getIntent().getStringExtra("fixture_id");
            team_id = getActivity().getIntent().getIntExtra("team_id",0);
            user_id = getActivity().getIntent().getStringExtra("user_id");
            user_name = getActivity().getIntent().getStringExtra("user_name");
            if (team_id != 0) {
                chatRoom =mDatabase.getReference().child("chats").child(fixture_id).child(team_id+"");
                chatRoom.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        updateChatConversation(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        updateChatConversation(dataSnapshot);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        }
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
                //messages.add(new Message(new User("1","Peter","asjhajdhaksj"), Calendar.getInstance().getTimeInMillis(),et_message_box.getText().toString().trim()));
                //mMessageAdapter.notifyDataSetChanged();
                sendMessage(et_message_box.getText().toString().trim());
                et_message_box.setText("");
                if (!mMessageRecycler.canScrollVertically(1)||mMessageRecycler.canScrollVertically(1))
                {if (mMessageAdapter.getItemCount()>0){
                    mMessageRecycler.getLayoutManager().smoothScrollToPosition(mMessageRecycler, null, mMessageAdapter.getItemCount() - 1);
                }
                }
            }
        }
    }
    //======================================================
    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View time, View view, int position) {
            if (time.getVisibility()==View.VISIBLE){
                time.startAnimation(anim_scale_down);
                time.setVisibility(View.INVISIBLE);

            }

            else{
                time.startAnimation(anim_scale_up);
                time.setVisibility(View.VISIBLE);
            }

        }
    };
    //======================================================
    private void updateChatConversation(DataSnapshot dataSnapshot){
        String message, user_id , user_name;
        long send_date=0;
        User thisuser = new User("1",this.user_name,"no_prof_pic");
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            message = (String)((DataSnapshot)i.next()).getValue();
            send_date = (long)((DataSnapshot)i.next()).getValue();
            user_id = (String)((DataSnapshot)i.next()).getValue();
            user_name = (String)((DataSnapshot)i.next()).getValue();
            Log.v("Message","message :"+message+" userid:"+user_id+" username :"+user_name+ " senddate "+send_date);

            if (user_id.equals(this.user_id))
                messages.add(new Message(thisuser, Long.valueOf(send_date),message));
            else {
                messages.add(new Message(new User("2",user_name,"no_prof_pic"), Long.valueOf(send_date),message));
            }
            mMessageAdapter.notifyDataSetChanged();
        }
    }
    //======================================================
    private void sendMessage(String msg){
        String user_message_key;
        Map<String ,Object > map = new HashMap<String ,Object >();
        user_message_key = chatRoom.push().getKey();
        chatRoom.updateChildren(map);
        DatabaseReference message =chatRoom.child(user_message_key);
        Map<String ,Object> message_fields = new HashMap<String ,Object >();
        message_fields.put("message",msg);
        message_fields.put("user_id",user_id);
        message_fields.put("user_name",user_name);
        message_fields.put("send_date", Calendar.getInstance().getTimeInMillis());
        message.updateChildren(message_fields);
    }
    //======================================================
}
