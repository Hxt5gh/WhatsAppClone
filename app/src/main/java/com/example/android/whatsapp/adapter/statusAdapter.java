package com.example.android.whatsapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devlomi.circularstatusview.CircularStatusView;
import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.models.status;
import com.example.android.whatsapp.models.userStatus;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class statusAdapter  extends RecyclerView.Adapter<statusAdapter.statusAdapterViewholder>
{

    Context context;
    ArrayList<userStatus> list;

    public statusAdapter(Context context, ArrayList<userStatus> list) {
        this.context = context;
        this.list = list;
    }
    public statusAdapter() {

    }

    @NonNull
    @Override
    public statusAdapterViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_item_view , parent , false);
        return new statusAdapterViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statusAdapterViewholder holder, int position) {



        int p = position;
        userStatus   userStatus = list.get(position);
        Log.d("TAG", "onBindViewHolder: "  +userStatus.getSenderUid());

        if (list.get(p).getSenderUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.statusView.setVisibility(View.GONE);
            holder.imageViewstatus.setVisibility(View.GONE);
        }


            status lastStatus =  userStatus.getStatusList().get(userStatus.getStatusList().size() - 1);
            Glide.with(context).load(lastStatus.getImageUri()).into(holder.imageViewstatus);

            holder.statusView.setPortionsCount(userStatus.getStatusList().size());



            holder.statusView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ArrayList<status> status = list.get(p).getStatusList();



                    ArrayList<MyStory> myStories = new ArrayList<>();


                    for (status s : userStatus.getStatusList())
                    {
                        myStories.add(new MyStory(s.getImageUri()));
                    }


                    new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                            .setStoriesList(myStories) // Required
                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                            .setTitleText(list.get(p).getName()) // Default is Hidden
                            //   .setSubtitleText("Damascus") // Default is Hidden
                            //   .setTitleLogoUrl("some-link") // Default is Hidden
                            .setStoryClickListeners(new StoryClickListeners() {
                                @Override
                                public void onDescriptionClickListener(int position) {
                                    //your action
                                }

                                @Override
                                public void onTitleIconClickListener(int position) {
                                    //your action
                                }
                            }) // Optional Listeners
                            .build() // Must be called before calling show method
                            .show();

                }
            });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  statusAdapterViewholder extends RecyclerView.ViewHolder {

    CircularStatusView statusView;
    ImageView imageViewstatus;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;

        public statusAdapterViewholder(@NonNull View itemView) {
            super(itemView);
            statusView = itemView.findViewById(R.id.circular_status_view);
            imageViewstatus = itemView.findViewById(R.id.statusimage);
            recyclerView = itemView.findViewById(R.id.friendsStatus);
            relativeLayout = itemView.findViewById(R.id.status_image_layout);

        }
    }
}
