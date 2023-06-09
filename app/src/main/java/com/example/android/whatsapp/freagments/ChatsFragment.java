package com.example.android.whatsapp.freagments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.adapter.viewAdapter;
import com.example.android.whatsapp.databinding.FragmentChatsBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {


    private  FragmentChatsBinding binding;
    private viewAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private ValueEventListener valueEventListener;
    private  ArrayList<UsersClass> list;
    private static final String LLL = "ChatsFragment";
    private myTask task;
    private download thread;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        task = new myTask();
       task.execute();

        thread = new download();
       // thread.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");



//                mRef.addChildEventListener(new ChildEventListener() {   //
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {  // call if data is present
//
////                Toast.makeText(MainActivity.this, "new data added", Toast.LENGTH_SHORT).show();
//                UsersClass person = snapshot.getValue(UsersClass.class);
//
//                person.setuId(snapshot.getKey());
//                list.add(person);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
////                person person = snapshot.getValue(com.example.android.myapplication.person.class);
////                person.setuID(snapshot.getKey());
////                list.remove(person);
////                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater , container , false);
//        return inflater.inflate(R.layout.fragment_chats, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new viewAdapter(getContext() , list);

        binding.idrecyclerview.setAdapter(adapter);
        binding.idrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    class myTask extends AsyncTask<Void , Void , Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            mRef.addChildEventListener(new ChildEventListener() {   //
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {  // call if data is present

//                Toast.makeText(MainActivity.this, "new data added", Toast.LENGTH_SHORT).show();
                    UsersClass person = snapshot.getValue(UsersClass.class);

                    person.setuId(snapshot.getKey());
                    list.add(person);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

//                person person = snapshot.getValue(com.example.android.myapplication.person.class);
//                person.setuID(snapshot.getKey());
//                list.remove(person);
//                adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return null;
        }
    }

   public  class download extends Thread
    {
        @Override
        public void run() {
            super.run();
            download();
        }
        void download()
        {
            mRef.addChildEventListener(new ChildEventListener() {   //
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {  // call if data is present

//                Toast.makeText(MainActivity.this, "new data added", Toast.LENGTH_SHORT).show();
                    UsersClass person = snapshot.getValue(UsersClass.class);

                    person.setuId(snapshot.getKey());
                    list.add(person);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

//                person person = snapshot.getValue(com.example.android.myapplication.person.class);
//                person.setuID(snapshot.getKey());
//                list.remove(person);
//                adapter.notifyDataSetChanged();
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

    @Override
    public void onStop() {
        super.onStop();
        task.cancel(true);
    }
}