package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInMemeActivity extends AppCompatActivity {

    private TextView greeting;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button logoutButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_meme);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        greeting = (TextView) findViewById(R.id.LoggedIn_greeting_textview);
        greeting.setText("Hello, " + user.getDisplayName() + "!");
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AnonymousMemeActivity.class);
        startActivity(intent);
    }
}
