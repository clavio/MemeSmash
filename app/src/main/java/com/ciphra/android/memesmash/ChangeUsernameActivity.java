package com.ciphra.android.memesmash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangeUsernameActivity extends AppCompatActivity {

    EditText firstUserBox;
    EditText secondUserBox;
    Button submitUsernameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        submitUsernameButton = findViewById(R.id.submit_username_button);
        submitUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNames();
            }
        });


    }

    private void submitNames(){
        Context context = getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUsernameActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        builder.setTitle("Change Name")
                .setMessage("Are you sure you want to change your name?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        changeName();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void changeName(){
        firstUserBox = findViewById(R.id.first_username_box);
        secondUserBox = findViewById(R.id.second_username_box);

        String firstUsername = firstUserBox.getText().toString();
        String secondUsername = secondUserBox.getText().toString();

        if(!firstUsername.equals(secondUsername)){
            Toast.makeText(getApplicationContext(), "Usernames must match!",
                    Toast.LENGTH_LONG).show();
        }
        else{
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstUsername)
                    .build();
            user.updateProfile(profileUpdates);
            Toast.makeText(getApplicationContext(), "Username succesfully changed!",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }
    }
}
