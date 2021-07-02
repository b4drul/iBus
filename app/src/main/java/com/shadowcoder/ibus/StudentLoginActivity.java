package com.shadowcoder.ibus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, resetPass;
    private Button mLogin, mbackToHome,cancelDialog , resetPassDialog;

    private TextView mForgetPassword, mRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(StudentLoginActivity.this, StudentMainMenuActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };


        mEmail = (EditText) findViewById(R.id.emailDriver);
        mPassword = (EditText) findViewById(R.id.passwordDriver);
        mLogin = (Button) findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        //mbackToHome = findViewById(R.id.backToHomeStudent);
        mForgetPassword = findViewById(R.id.forget_passwordStudent);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
//                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(StudentLoginActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (!task.isSuccessful()){
//                            Toast.makeText(StudentLoginActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
//                        }else{
//                            String user_id = mAuth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(user_id);
//                            current_user_db.setValue(true);
//                        }
//                    }
//                });

                Intent intent = new Intent(StudentLoginActivity.this, StudentSignUpActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate Login Info
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    final String email = mEmail.getText().toString();
                    final String password = mPassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(StudentLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(StudentLoginActivity.this, "Sign in error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(StudentLoginActivity.this);
                //We have added a title in the custom layout. So let's disable the default title.
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
                dialog.setCancelable(true);
                //Mention the name of the layout of your custom dialog.
                dialog.setContentView(R.layout.forget_password);

                //Initializing the views of the dialog.
                resetPass = dialog.findViewById(R.id.editTextResetPassword);
                resetPassDialog = dialog.findViewById(R.id.resetButton);

                resetPassDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get email and send reset link
                        String userEmail = resetPass.getText().toString();
                        mAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplication(), "Password reset email sent", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplication(), "Failed! Please try again", Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

//        mbackToHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StudentLoginActivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        });


    }


    private Boolean validateEmail() {
        String val = mEmail.getText().toString();
        if (val.isEmpty()) {
            mEmail.setError("Field cannot be empty");
            return false;
        } else {
            mEmail.setError(null);
            //mEmail.requestFocus();
            //mEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = mPassword.getText().toString();
        if (val.isEmpty()) {
            mPassword.setError("Field cannot be empty");
            return false;
        } else {
            mPassword.setError(null);
            //mPassword.requestFocus();
            //mPassword.setErrorEnabled(false);
            return true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed() {
        Intent intent_back = new Intent(StudentLoginActivity.this, HomeActivity.class);
        startActivity(intent_back);
        finish();
    }
}