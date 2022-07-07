package com.example.nishhir.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nishhir.R;
import com.example.nishhir.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends Activity {
    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    String mVisible1 = "visible";
    String mVisible2 = "visible";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        //to go to login activity
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //set the visibility
        binding.visible1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVisible1.equals("visible")) {
                    binding.pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.visible1.setImageResource(R.drawable.hidden);
                    mVisible1 = "invisible";
                } else {
                    binding.pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.visible1.setImageResource(R.drawable.visibility);
                    mVisible1 = "visible";
                }
            }
        });
        binding.visible2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVisible2.equals("visible")) {
                    binding.cPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.visible2.setImageResource(R.drawable.hidden);
                    mVisible2 = "invisible";
                } else {
                    binding.cPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.visible2.setImageResource(R.drawable.visibility);
                    mVisible2 = "visible";
                }
            }
        });
        //to create account
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               createUser();
            }
        });
    }
    public void createUser() {
        String email = binding.email.getText().toString().trim();
        String pass = binding.pass.getText().toString();
        String cPass = binding.cPass.getText().toString();

        if(TextUtils.isEmpty(email)){
            binding.email.setError("This field is required");
            binding.email.requestFocus();
        } else if(TextUtils.isEmpty(pass)) {
            binding.pass.setError("This field is required");
            binding.pass.requestFocus();
        } else if(pass.length() < 6) {
            binding.pass.setError("Password contains minimum 6 characters");
        } else if(!pass.equals(cPass)) {
            binding.cPass.setError("Passwords doesn't match");
        } else {
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setTitle("Registering");
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //creating user
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                        finishAffinity();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
