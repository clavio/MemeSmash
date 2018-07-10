package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {

    private Button logoutButton;
    private Button changeNameButton;
    private Button backButton;
    private Button myMemesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        logoutButton = findViewById(R.id.profile_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        changeNameButton = findViewById(R.id.username_button);
        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNameChange();
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        myMemesButton = findViewById(R.id.my_memes_button);
        myMemesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyMemes();
            }
        });

    }

    private void goToNameChange(){
        Intent intent = new Intent(this, ChangeUsernameActivity.class);
        startActivity(intent);
    }

    private void goToMyMemes(){
        Intent intent = new Intent(this, OwnMemesActivity.class);
        startActivity(intent);
    }

    private void goBack(){
        Intent intent = new Intent(this, MemeActivity.class);
        startActivity(intent);
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager ln = LoginManager.getInstance();
        ln.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
