package com.example.android.whatsapp.freagments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.R;

import com.example.android.whatsapp.adapter.statusAdapter;
import com.example.android.whatsapp.chatActivities.ChatDetailActivity;
import com.example.android.whatsapp.databinding.FragmentStatusBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.example.android.whatsapp.models.messageModel;
import com.example.android.whatsapp.models.status;
import com.example.android.whatsapp.models.userStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;


public class StatusFragment extends Fragment {


    private FragmentStatusBinding binding;
    private ArrayList<userStatus> statusList;
    private statusAdapter statusAdapter;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
    private UsersClass usersClass;
    private status sStatus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusList = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        context = getContext();
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading...");
        dialog.setCancelable(false);

        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersClass = snapshot.getValue(UsersClass.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    statusList.clear();
                    for (DataSnapshot dataSnapshot :snapshot.getChildren())
                    {
                        userStatus userStatus = new userStatus();
                        userStatus.setName(dataSnapshot.child("name").getValue(String.class));
                        userStatus.setLastUpdated(dataSnapshot.child("lastUpdate").getValue(Long.class));
                        userStatus.setSenderUid(dataSnapshot.child("senderUid").getValue(String.class));


                        //inside statususes
                        ArrayList<status> statuses = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child("statuses").getChildren())
                        {
                            status status = dataSnapshot1.getValue(com.example.android.whatsapp.models.status.class);
                            statuses.add(status);
                            Log.d("TAG", "status list " + status.getImageUri());

                        }



                        userStatus.setStatusList(statuses);
                        statusList.add(userStatus);
                    }
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatusBinding.inflate(inflater , container , false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();


        statusAdapter = new statusAdapter(context ,  statusList);
       // selfStatusAdapter = new SelfStatusAdapter(context , statusList);

        binding.friendsStatus.setLayoutManager(new LinearLayoutManager(context));
        binding.friendsStatus.setAdapter(statusAdapter);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent , 476);
            }
        });

        binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingStatus();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 476)
        {
            if (data != null)
            {
                Uri uriImage = data.getData();
                uploadImageWithCompress(uriImage);
            }
        }
    }

    private Bitmap bmp;
    private ByteArrayOutputStream baos;
    private void uploadImageWithCompress(Uri imageUri ) {
        dialog.show();


        // images are stored with timestamp as their name
        String timestamp = "" + System.currentTimeMillis();



        bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver() , imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        baos = new ByteArrayOutputStream();

        // here we can choose quality factor
        // in third parameter(ex. here it is 25)
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        byte[] fileInBytes = baos.toByteArray();
        Calendar calendar = Calendar.getInstance();

        Date date = new Date();
        storageReference.child("status").child(date.getTime()+"").putBytes(fileInBytes).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful())
                {
                    storageReference.child("status").child(date.getTime()+"").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            userStatus status = new userStatus();
                            status.setName(usersClass.getUserName());
                            status.setLastUpdated(date.getTime());
                            status.setSenderUid(FirebaseAuth.getInstance().getUid());


                            HashMap<String , Object> obj = new HashMap<>();
                            obj.put("name" , status.getName());
                            obj.put("lastUpdate" , status.getLastUpdated());
                            obj.put("senderUid" , status.getSenderUid());

                            databaseReference.child("Status").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);


                            sStatus = new status();
                            sStatus.setImageUri(uri.toString());
                            sStatus.setTimeStam(status.getLastUpdated());

                            databaseReference.child("Status").child(FirebaseAuth.getInstance().getUid()).child("statuses").push().setValue(sStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });

                            dialog.dismiss();
                            Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show();
                          //  databaseReference.child("Status").child(FirebaseAuth.getInstance().getUid()).
                        }
                    });

                }
            }
        });

    }


    private ArrayList<userStatus>  llList;
    private ArrayList<status> sList;

    private void settingStatus() {

//                    ArrayList<userStatus>  llList = new ArrayList<>();
//                    ArrayList<status> sList = new ArrayList<>();
                llList = new ArrayList<>();
                 sList = new ArrayList<>();

        databaseReference.child("Status").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (sList != null)
                    {
                        sList.clear();
                        llList.clear();
                    }

                    String nam = snapshot.child("name").getValue(String.class);
                    Long last = snapshot.child("lastUpdate").getValue(Long.class);
                    String sendUid = snapshot.child("senderUid").getValue(String.class);
                    Log.d("TAG", "onDataChange: " +nam  +"  " +last  +" " +sendUid);




                    for (DataSnapshot dataSnapshot : snapshot.child("statuses").getChildren())
                    {
                      status  status = dataSnapshot.getValue(status.class);
                      String  uri = status.getImageUri();
                      Long time = status.getTimeStam();
                      Log.d("TAG", "onDataChange: " +uri +" \n " +time);

                        sList.add(new status(uri , time));
                        Glide.with(context).load(uri).into(binding.circleImageView);
                    }

                    userStatus mmStatus = new userStatus();
                    mmStatus.setName(nam);
                    mmStatus.setLastUpdated(last);
                    mmStatus.setStatusList(sList);


                    llList.add(mmStatus);

                    ArrayList<MyStory> myStories = new ArrayList<>();

                        int o = 1;
                    for (status man  : sList )
                    {
                        myStories.add( new MyStory(man.getImageUri()));
                        binding.circularStatusView.setPortionsCount(3);
                        o++;

                    }
                 //   Toast.makeText(context, o, Toast.LENGTH_SHORT).show();



                    new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                            .setStoriesList(myStories) // Required
                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                            .setTitleText(nam) // Default is Hidden
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}