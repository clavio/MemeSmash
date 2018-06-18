package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailRegistrationActivity extends AppCompatActivity {

    private Button mCancelButton;
    private Button mSubmitButton;
    private EditText emailEditText;
    private EditText emailPasswordEditText;
    private EditText emailPasswordConfirmBox;
    private String TAG = "meme_smash";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);

        mCancelButton = findViewById(R.id.cancel_email_registration);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });
        mSubmitButton = findViewById(R.id.submit_email_registration);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText = (EditText) findViewById(R.id.email_registration_box);
                emailPasswordEditText = (EditText) findViewById(R.id.email_password_box);
                emailPasswordConfirmBox = (EditText) findViewById(R.id.email_password_confirm_box);

                if(!emailPasswordEditText.getText().toString().equals(emailPasswordConfirmBox.getText().toString())){
                    Toast.makeText(EmailRegistrationActivity.this, "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    emailRegistration(emailEditText.getText().toString(), emailPasswordEditText.getText().toString());
                }
            }
        });
    }


    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void emailRegistration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            mAuth.signOut();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(EmailRegistrationActivity.this);
                            builder1.setMessage("Registration successful.  Follow the link emailed to you to verify your account.");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            returnToMain();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailRegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}