package com.example.nishhir.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.nishhir.R;
import com.example.nishhir.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.chrono.MinguoChronology;

public class SignInActivity extends Activity {
    ActivitySignInBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String mVisible = "visible";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging In");
        dialog.setCancelable(false);

        if(user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //to make password visible
        binding.visible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVisible.equals("visible")) {
                    binding.cPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.visible.setImageResource(R.drawable.hidden);
                    mVisible = "invisible";
                } else {
                    binding.cPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.visible.setImageResource(R.drawable.visibility);
                    mVisible = "visible";
                }
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String email = binding.email.getText().toString().trim();
                String pass = binding.cPass.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    binding.email.setError("This field is required");
                    binding.email.requestFocus();
                } else if(TextUtils.isEmpty(pass)) {
                    binding.cPass.setError("This field is required");
                    binding.cPass.requestFocus();
                } else {
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
