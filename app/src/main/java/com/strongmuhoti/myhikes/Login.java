package com.strongmuhoti.myhikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Login extends AppCompatActivity {

    EditText email, password;
    TextView forgotPassword;
    Button register, login;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


try {
    login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkField(email);
            checkField(password);

            if (valid){
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        assert user != null;
                        if (user.isEmailVerified()){
                            Toast.makeText(Login.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                            CheckUserAccessLevel(authResult.getUser().getUid());
                            progressBar.setVisibility(View.GONE);
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(Login.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Error: No such account. Please check credentials or Create new account!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    });
} catch (Exception e){
    e.printStackTrace();
}



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });
    }

    private void CheckUserAccessLevel(String uid) {
        DocumentReference dr = fStore.collection("Users").document(uid);
        //Extract data from document
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                //Identify the user access level
                if (documentSnapshot.getString("isAdmin") != null){
                    //User is Admin
                    startActivity(new Intent(getApplicationContext(), DataUploader.class));
                    finish();
                }

                if (documentSnapshot.getString("isUser") != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().trim().isEmpty()){
            textField.setError("Error");
            textField.requestFocus();
            valid = false;

        } else {
            valid = true;
        }

        return valid;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            DocumentReference dr = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot.getString("isAdmin") != null){
//                        startActivity(new Intent(getApplicationContext(), DataUploader.class));
//                        finish();
//                    } if (documentSnapshot.getString("isUser") != null){
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    FirebaseAuth.getInstance().signOut();
//                    startActivity(new Intent(getApplicationContext(), Login.class));
//                    finish();
//                }
//            });
//        }
//    }
}