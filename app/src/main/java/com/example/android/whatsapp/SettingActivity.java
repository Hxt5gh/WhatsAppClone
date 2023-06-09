package com.example.android.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.whatsapp.databinding.ActivitySettingBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private  Uri uFile = null;
    private ProgressDialog progressDialog;
    String username;
    String status;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Profile_Pic");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile Updating");
        progressDialog.setMessage("Please Wait foe a Second... ");

        binding.btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this , MainActivity.class);
                startActivity(i);
            }
        });


        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent  , 33);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploading();
            }
        });

        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersClass data = snapshot.getValue(UsersClass.class);
                Log.d("TAG", "onDataChange: "  +data.getProfilepic()  +"  "   +data.getUserName() +"  "   +data.getAbout());
                if(data.getProfilepic() != null)
                {
                Glide.with(getApplicationContext()).load(data.getProfilepic()).into(binding.uploadImage);
                }
                else
                {
                    binding.uploadImage.setImageResource(R.drawable.user);
                }
                binding.usernameTextview.setText(data.getUserName());
                binding.aboutTextview.setText(data.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.nameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nameTextView.setVisibility(View.VISIBLE);
                username = binding.nameTextView.getText().toString();

            }
        });

        binding.aboutEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.statusTextView.setVisibility(View.VISIBLE);

                status = binding.aboutTextview.getText().toString();

            }
        });

        binding.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri urii = Uri.parse("android.resource://com.example.android.whatsapp/drawable/user");


                progressDialog.show();
                storageReference.child(FirebaseAuth.getInstance().getUid()).putFile(urii).
                        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilepic").setValue(uri.toString());



                                        //binding.uploadImage.setImageResource(R.drawable.user);
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                                    }
                                });

                            }
                        });

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null)
        {
            uFile = data.getData();
            binding.uploadImage.setImageURI(uFile);

            progressDialog.show();
            storageReference.child(FirebaseAuth.getInstance().getUid()).putFile(uFile).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profilepic").setValue(uri.toString());

                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                                }
                            });

                        }
                    });

        }

    }



    private void uploading() {

        username = binding.nameTextView.getText().toString();
        status = binding.statusTextView.getText().toString();
        if (TextUtils.isEmpty(username)  && binding.nameTextView.getVisibility() == View.VISIBLE)
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(status) && binding.statusTextView.getVisibility() == View.VISIBLE)
        {
            Toast.makeText(this, "About Your Self", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(username)   && !TextUtils.isEmpty(status) )
        {

                        progressDialog.show();


                        storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                binding.nameTextView.setVisibility(View.GONE);
                                binding.statusTextView.setVisibility(View.GONE);

                                databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("userName").setValue(username);
                                databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("about").setValue(status);
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                            }
                        });
        }
        else if ( !TextUtils.isEmpty(username))
        {
            progressDialog.show();


            storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    binding.nameTextView.setVisibility(View.GONE);
                    binding.statusTextView.setVisibility(View.GONE);

                    databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("userName").setValue(username);
                    //databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("about").setValue(status);
                    progressDialog.dismiss();
                    Toast.makeText(SettingActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                }
            });
        }
        else if (!TextUtils.isEmpty(status))
        {
            progressDialog.show();


            storageReference.child(FirebaseAuth.getInstance().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    binding.nameTextView.setVisibility(View.GONE);
                    binding.statusTextView.setVisibility(View.GONE);

                   // databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("userName").setValue(username);
                    databaseReference.child("Users").child(FirebaseAuth.getInstance().getUid()).child("about").setValue(status);
                    progressDialog.dismiss();
                    Toast.makeText(SettingActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                }
            });
        }

    }

}