package com.games.peter.project_live_football_tactics.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.games.peter.project_live_football_tactics.Class.Message;
import com.games.peter.project_live_football_tactics.Listener.OnItemClickListener;
import com.games.peter.project_live_football_tactics.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Peter on 18/4/2018.
 */

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<Message> mMessageList;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public MessageListAdapter(Context context, List<Message> messageList, OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        mContext = context;
        mMessageList = messageList;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public int getItemCount() {
        Log.v("getItemCount() : ", "SIZE OF LIST:"+mMessageList.size());
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mMessageList.get(position);

//        if (message.getSender().getId().equals(SendBird.getCurrentUser().getUserId())) {
        if (message.getSender().getId().equals("1")) {
//            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_user, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_message_others, parent, false);
            return new ReceivedMessageHolder(view);
        }


        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                ((SentMessageHolder) holder).messageText.setOnClickListener(new OnItemClickListener(((SentMessageHolder) holder).timeText,position, onItemClickCallback));
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                ((ReceivedMessageHolder) holder).messageText.setOnClickListener(new OnItemClickListener(((ReceivedMessageHolder) holder).timeText,position, onItemClickCallback));
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date(message.getCreatedAt())));
            //timeText.setText(DateUtils.formatDateTime(mContext,message.getCreatedAt(),0));
            timeText.setText(new SimpleDateFormat("hh:mm a").format(new Date(message.getCreatedAt())));
            Log.v("SETTING_MSG","CREATE NEW MSG");
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            //timeText.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date(message.getCreatedAt())));
            //timeText.setText(DateUtils.formatDateTime(mContext,message.getCreatedAt(),0));
            timeText.setText(new SimpleDateFormat("hh:mm a").format(new Date(message.getCreatedAt())));
            nameText.setText(message.getSender().getName());
            Glide.with(MessageListAdapter.this.mContext).load(message.getSender().getProfilePictureUrl()).into(profileImage);
            Log.v("SETTING_MSG","RECEIVED NEW MSG");
            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}