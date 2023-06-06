package com.example.android.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;

import com.example.android.whatsapp.adapter.fragmentAdapter;
import com.example.android.whatsapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private String[] pageElements;
    private fragmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        pageElements= new String[]{"Chats", "Status", "Call"};


        adapter = new fragmentAdapter(this);
        binding.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout , binding.viewPager2 , ((tab, position) -> tab.setText(pageElements[position]))).attach();






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menue , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.newGroup:

                break;

            case R.id.setting:
                break;

            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}