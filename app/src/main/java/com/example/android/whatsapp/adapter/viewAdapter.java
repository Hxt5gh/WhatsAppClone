package com.example.android.whatsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.chatActivities.ChatDetailActivity;
import com.example.android.whatsapp.models.UsersClass;

import java.util.ArrayList;

public class viewAdapter  extends RecyclerView.Adapter<viewAdapter.viewholder>
{

    ArrayList<UsersClass> list ;
    Context context;

    public viewAdapter(Context context ,ArrayList<UsersClass> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewlayout , null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        int p = position;

        holder.username.setText(list.get(position).getUserName());
        if (list.get(position).getProfilepic() != null)
        {
        Glide.with(context).load(list.get(position).getProfilepic()).into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.user);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , ChatDetailActivity.class);
                    intent.putExtra("image_url", list.get(p).getProfilepic());
                    intent.putExtra("User_name" , list.get(p).getUserName());

                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView username , lastmsg , time ;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profilePic);
            username = itemView.findViewById(R.id.username);
            lastmsg= itemView.findViewById(R.id.lastmessage);
            time = itemView.findViewById(R.id.idtime);
        }
    }
}
