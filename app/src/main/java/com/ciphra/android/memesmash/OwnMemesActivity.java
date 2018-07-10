package com.ciphra.android.memesmash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnMemesActivity extends AppCompatActivity {

    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_memes);
        ll = findViewById(R.id.my_memes_layout);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference fatherMeme = database.getReference("memes");

        fatherMeme.orderByChild("userId").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Meme meme = (Meme) dataSnapshot.getValue(Meme.class);
                ImageView iV = new ImageView(getApplicationContext());
                Glide
                        .with(OwnMemesActivity.this)
                        .load(meme.getPictureId())
                        .into(iV);
                TextView tv = new TextView(getApplicationContext());
                tv.setText("Score: " + meme.getScore());
                ll.addView(iV);
                ll.addView(tv);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
