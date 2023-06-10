package com.example.android.whatsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.chatActivities.ChatDetailActivity;
import com.example.android.whatsapp.models.UsersClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        if (list.get(p).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.username.setVisibility(View.GONE);
            holder.lastmsg.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.lastmsg.setVisibility(View.GONE);
        }

        FirebaseDatabase.getInstance().getReference().
                child("Chats").
                child(FirebaseAuth.getInstance().getUid() + list.get(p).getuId()).
                orderByChild("lastMessage").limitToLast(1).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren())
                        {
                            for ( DataSnapshot da:snapshot.getChildren() ){

                                holder.lastmsg.setText(da.child("message").getValue().toString());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.username.setText(list.get(p).getUserName());


        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(100, 100); // resize does not respect aspect ratio



        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

        if (list.get(p).getProfilepic() != null)
        {
        Glide.with(context).load(list.get(p).getProfilepic()).apply(requestOptions).into(holder.imageView);
            Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.user);
        }
            }
        });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , ChatDetailActivity.class);
                    intent.putExtra("receiverUID" , list.get(p).getuId());
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
           // time = itemView.findViewById(R.id.idtime);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
    }
}
