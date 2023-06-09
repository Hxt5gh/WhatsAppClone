package com.example.android.whatsapp.chatActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.adapter.chatAdapter;
import com.example.android.whatsapp.databinding.ActivityGroupChatBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.example.android.whatsapp.models.messageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupChatActivity extends AppCompatActivity {

    private ActivityGroupChatBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private ArrayList<messageModel> list;
    private  String n = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();


        binding.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupChatActivity.this , MainActivity.class);
                startActivity(i);
            }
        });

        chatAdapter chatAdapter = new chatAdapter(list , this);
        binding.recyclerView.setAdapter(chatAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String  senderUid = mAuth.getUid();
        binding.btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = binding.editTextTextPersonName.getText().toString();
                binding.editTextTextPersonName.setText("");
                if(!TextUtils.isEmpty(data)) {






                    messageModel model = new messageModel(senderUid, data);
                    model.setLastMessage(System.currentTimeMillis());

                    database.getReference().child("GroupChats").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {



                        }
                    });

                }


            }
        });


        mRef.child("GroupChats").addChildEventListener(new ChildEventListener() {
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






    }
}