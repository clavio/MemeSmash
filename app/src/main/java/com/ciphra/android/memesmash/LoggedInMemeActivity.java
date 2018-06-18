package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInMemeActivity extends AppCompatActivity {

    private TextView greeting;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private TextView logoutButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_meme);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        greeting = (TextView) findViewById(R.id.LoggedIn_greeting_textview);
        greeting.setText("Hello, " + user.getDisplayName() + "!");
        logoutButton = (TextView) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager ln = LoginManager.getInstance();
        ln.logOut();
        Intent intent = new Intent(this, AnonymousMemeActivity.class);
        startActivity(intent);
    }
}