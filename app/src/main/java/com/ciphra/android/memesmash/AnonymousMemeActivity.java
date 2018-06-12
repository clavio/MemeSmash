package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AnonymousMemeActivity extends AppCompatActivity {

    private TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_meme);

        mLoginTextView = findViewById(R.id.anonymous_greeting_textview);
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });
    }

    public void goLogin(){
        Intent intent = new Intent(this, LoginRegister.class);
        startActivity(intent);
    }


}
