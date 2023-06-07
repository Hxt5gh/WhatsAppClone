package com.example.android.whatsapp.chatActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.whatsapp.LoginActivity;
import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.adapter.chatAdapter;
import com.example.android.whatsapp.databinding.ActivityChatDetailBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.example.android.whatsapp.models.messageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private ArrayList<messageModel> list;
    private static final String MYTAG = "ChatDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();


        String  senderUid = mAuth.getUid();
        Intent intent = getIntent();
        String receverUid = intent.getStringExtra("senderuid" );
        String  userprofile =    intent.getStringExtra("image_url");
        String username = intent.getStringExtra("User_name" );

        binding.username.setText(username);
        if (userprofile != null)
        {
            Glide.with(getApplicationContext()).load(userprofile).into(binding.profilePic);
        }
        else {
           binding.profilePic.setImageResource(R.drawable.user);
        }

        binding.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });

        chatAdapter chatAdapter = new chatAdapter(list , this);
        binding.recyclerView.setAdapter(chatAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final  String senderRoom = senderUid + receverUid;
        final  String receiverRoom = receverUid  + senderUid;




        mRef.child("Chats").child(senderRoom).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    messageModel model = snapshot.getValue(messageModel.class);
                    list.add(model);
                    chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        binding.btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatDetailActivity.this, "send", Toast.LENGTH_SHORT).show();
                Log.d(MYTAG, "onCreate: sender " +senderRoom);
                Log.d(MYTAG, "onCreate: recever " +receiverRoom);



                String data = binding.editTextTextPersonName.getText().toString();
                   binding.editTextTextPersonName.setText("");
                   if(!TextUtils.isEmpty(data)) {


                       messageModel model = new messageModel(senderUid, data);
                       model.setLastMessage(System.currentTimeMillis());

                       database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
//                                       inserting in receverroom
                                       database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void unused) {
                                                       Toast.makeText(ChatDetailActivity.this, "", Toast.LENGTH_SHORT).show();

                                                   }
                                               });


                                   }
                               });

                   }


            }
        });


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.chatmenue , menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.idInfo:

                break;

            case R.id.idSearch:
                break;

            case R.id.idWalpaper:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}