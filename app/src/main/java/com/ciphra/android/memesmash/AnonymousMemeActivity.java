package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AnonymousMemeActivity extends AppCompatActivity {

    private TextView mLoginTextView;
    private Button memeAButton;
    private Button memeBButton;
    Meme memeA;
    Meme memeB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_meme);
        memeAButton = findViewById(R.id.anonymous_memebutton_a);
        memeAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScore(true);
            }
        });
        memeBButton = findViewById(R.id.anonymous_memebutton_b);
        memeBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScore(false);
            }
        });
        mLoginTextView = findViewById(R.id.anonymous_greeting_textview);
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }

        });

        setTwoMemes();

    }

    public void goLogin(){
        Intent intent = new Intent(this, LoginRegister.class);
        startActivity(intent);
    }

    public void setTwoMemes(){
        Random random = new Random();
        int indexOne = 0;
        int indexTwo = 0;

        while(indexOne == indexTwo){
            indexOne = random.nextInt(100);
            indexTwo = random.nextInt(100);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference firstMemeRef = database.getReference("meme" + String.valueOf(indexOne));
        firstMemeRef.addListenerForSingleValueEvent(memeListenerA);
        DatabaseReference secondMemeRef = database.getReference("meme" + String.valueOf(indexTwo));
        secondMemeRef.addListenerForSingleValueEvent(memeListenerB);

    }

    ValueEventListener memeListenerA = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
           memeA = (Meme) dataSnapshot.getValue(Meme.class);
           memeAButton.setText("Id: " + String.valueOf(memeA.getId()) + " Score: " + String.valueOf(memeA.getScore()));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener memeListenerB = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            memeB = dataSnapshot.getValue(Meme.class);
            memeBButton.setText("Id: " + String.valueOf(memeB.getId()) + " Score: " + String.valueOf(memeB.getScore()));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void changeScore(boolean isA){
        int rawDiff = memeA.getScore() - memeB.getScore();
        int tempMemeAScore = memeA.getScore();
        int tempMemeBScore = memeB.getScore();
        int scoreChange = 0;
        rawDiff = rawDiff + 800;
        if(rawDiff < 0)
            rawDiff = 0;
        if(rawDiff > 1600)
            rawDiff = 1600;


        if(isA){
            scoreChange = 16-rawDiff/100;
            memeA.setScore(tempMemeAScore+scoreChange);
            memeB.setScore(tempMemeBScore-scoreChange);
        }
        else{
            scoreChange = scoreChange + rawDiff/100;
            memeA.setScore(tempMemeAScore-scoreChange);
            memeB.setScore(tempMemeBScore+scoreChange);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference referenceA = database.getReference("meme" + String.valueOf(memeA.getId()));
        referenceA.setValue(memeA);

        DatabaseReference referenceB = database.getReference("meme" + String.valueOf(memeB.getId()));
        referenceB.setValue(memeB);
        setTwoMemes();
    }


}
