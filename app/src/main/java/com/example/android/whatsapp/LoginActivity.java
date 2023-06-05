package com.example.android.whatsapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.whatsapp.databinding.ActivityLoginBinding;
import com.example.android.whatsapp.models.UsersClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 99 ;
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef ;
    private FirebaseUser firebaseUser;
    private  GoogleSignInOptions gso;
    private GoogleSignInClient  mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            Intent intent1 = new Intent(LoginActivity.this , MainActivity.class);
            startActivity(intent1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , SignupActivity.class);
                startActivity(intent);
            }
        });




        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressCircular.setVisibility(View.VISIBLE);
                String email = binding.emailTextView.getText().toString();
                String password  = binding.passwordTextView.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    binding.progressCircular.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    binding.progressCircular.setVisibility(View.GONE);
                    return;
                }


                firebaseAuth.signInWithEmailAndPassword(binding.emailTextView.getText().toString() ,binding.passwordTextView.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    binding.progressCircular.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "called called", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(LoginActivity.this , MainActivity.class);
                                    startActivity(intent1);
                                }
                                else
                                {
                                    binding.progressCircular.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Fail ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        binding.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(LoginActivity.this , PhoneActivity.class);
                   startActivity(intent);
            }
        });

    }



    //google sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);


                FirebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
                //  updateUI(null);
            }

        }
    }
    private void FirebaseAuthWithGoogle(String idToken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken , null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UsersClass usersClass = new UsersClass();
                            usersClass.setuId(user.getUid());
                            usersClass.setMail(user.getEmail());
                            usersClass.setUserName(user.getDisplayName());
                            usersClass.setProfilepic(user.getPhotoUrl().toString());
                            myRef.child("Users").child(user.getUid()).setValue(usersClass);

                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {

                        }

                    }
                });

    }
}