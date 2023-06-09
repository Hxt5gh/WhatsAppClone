package com.example.android.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.whatsapp.databinding.ActivitySugnupBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar;

public class SignupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 99;
    private ActivitySugnupBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySugnupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
         myRef = database.getReference();



        binding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });


        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(SignupActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                binding.progressCircular.setVisibility(View.VISIBLE);

                String name = binding.nameTextView.getText().toString();
                String email = binding.emailTextView.getText().toString();
                String password  = binding.passwordTextView.getText().toString();
                if (TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignupActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    binding.progressCircular.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(SignupActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    binding.progressCircular.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    binding.progressCircular.setVisibility(View.GONE);
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(binding.emailTextView.getText().toString()  , binding.passwordTextView.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    binding.progressCircular.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this, "SuccessFully Sign Up ", Toast.LENGTH_SHORT).show();


                                    //sendind data to datbase
                                    UsersClass usersClass = new UsersClass(binding.nameTextView.getText().toString() ,
                                                                           binding.emailTextView.getText().toString() ,
                                                                           binding.passwordTextView.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    Toast.makeText(SignupActivity.this, "SuccessFull  agein ", Toast.LENGTH_LONG).show();
                                    myRef.child("Users").child(id).setValue(usersClass);

                                   Intent i = new Intent( SignupActivity.this ,MainActivity.class);
                                   startActivity(i);



                                }

                                else
                                {
                                    binding.progressCircular.setVisibility(View.GONE);
                                    Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });


    }


}