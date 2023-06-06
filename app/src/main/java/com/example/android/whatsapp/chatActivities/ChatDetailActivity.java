package com.example.android.whatsapp.chatActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.android.whatsapp.LoginActivity;
import com.example.android.whatsapp.MainActivity;
import com.example.android.whatsapp.R;
import com.example.android.whatsapp.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        Intent intent = getIntent();
        String  userprofile =    intent.getStringExtra("image_url");
        String username = intent.getStringExtra("User_name" );

        if (userprofile != null)
        {
            Glide.with(getApplicationContext()).load(userprofile).into(binding.profilePic);
        }
        else {
           binding.profilePic.setImageResource(R.drawable.user);
        }

        binding.username.setText(username);

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