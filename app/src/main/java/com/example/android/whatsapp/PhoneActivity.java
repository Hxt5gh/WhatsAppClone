package com.example.android.whatsapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.android.whatsapp.databinding.ActivityPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private static final String MYTAG = "MyTag";
    private ActivityPhoneBinding binding;
    private FirebaseAuth mAuth;
    private String verificationId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verifing Phone Number");
        progressDialog.setMessage("Wait few Seconds...");


        binding.otpTextView.setVisibility(View.GONE);
        binding.btnSubmit.setVisibility(View.GONE);
        binding.phonenoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otpTextView.setVisibility(View.GONE);
                binding.btnSubmit.setVisibility(View.GONE);
            }
        });




        binding.btnOtpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String phoneno = binding.phonenoTextView.getText().toString().trim();
                Log.d(MYTAG, "onClick: " +phoneno);
                if (TextUtils.isEmpty(phoneno))
                {
                    Toast.makeText(PhoneActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();


                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phoneno)       // Phone number to verify
                                .setTimeout(20L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(PhoneActivity.this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                                        Toast.makeText(PhoneActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {

                                        progressDialog.dismiss();
                                        Toast.makeText(PhoneActivity.this, "Fail to  Verify Phone Number ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);

                                        progressDialog.dismiss();
                                        verificationId = s;
                                        binding.otpTextView.setVisibility(View.VISIBLE);
                                        binding.btnSubmit.setVisibility(View.VISIBLE);
                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });





        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.otpTextView.getText().toString()))
                {
                    Toast.makeText(PhoneActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verificationId , binding.otpTextView.getText().toString());
                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(PhoneActivity.this , MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(PhoneActivity.this, "Successfully Regestered", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(PhoneActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



    }


}