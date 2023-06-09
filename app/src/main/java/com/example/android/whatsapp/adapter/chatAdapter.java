package com.example.android.whatsapp.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.whatsapp.R;
import com.example.android.whatsapp.models.messageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class chatAdapter  extends  RecyclerView.Adapter {

    private  ArrayList<messageModel> models;
    private Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;


    public chatAdapter(ArrayList<messageModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_item_view ,parent, false);
            return new senderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_item_view , parent , false);
            return new receiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if(models.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.CANADA);

        if (holder.getClass() == senderViewHolder.class)
        {
            ((senderViewHolder)holder).senderTxt.setText(models.get(position).getMessage());
            long time = models.get(position).getLastMessage();

            String t = dateFormat.format(time);
            ((senderViewHolder) holder).senderTime.setText(t);

            ((senderViewHolder)holder).senName.setText(models.get(position).getnName());


        }
        else
        {
            ((receiverViewHolder)holder).receiverText.setText(models.get(position).getMessage());
            long time = models.get(position).getLastMessage();
            String t = dateFormat.format(time);
            ((receiverViewHolder) holder).receiverTime.setText(t);

            ((receiverViewHolder)holder).recName.setText(models.get(position).getRecvierName());

        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class senderViewHolder extends RecyclerView.ViewHolder {

        TextView senderTxt , senderTime , senName;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTime = itemView.findViewById(R.id.senderTime);
            senderTxt = itemView.findViewById(R.id.senderText);
            senName = itemView.findViewById(R.id.senderName);
        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverText , receiverTime , recName;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverText = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            recName = itemView.findViewById(R.id.receiverName);

        }
    }
}
