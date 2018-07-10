package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class MemeActivity extends AppCompatActivity {

    private TextView mLoginTextView;
    private TextView mCreateMemeView;
    private ImageView memeAButton;
    private ImageView memeBButton;

    private boolean memeASettable = true;
    private boolean memeBSettable = true;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private int memeCount = 0;
    Meme memeA;
    Meme memeB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_meme);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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
        mLoginTextView.setText("Hello, " + user.getDisplayName() + "!");
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });
        mCreateMemeView = findViewById(R.id.create_a_meme);
        mCreateMemeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAMeme();
            }
        });
      //  loadMemes();
        updateCount();



    }

    public void createAMeme(){
        Intent intent = new Intent(this, MemeUploadActivity.class);
        startActivity(intent);
    }

    public void updateCount(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fatherMeme = database.getReference("memes");
        fatherMeme.addListenerForSingleValueEvent(getMemeCount);
    }


    public void setTwoMemes(){
        memeASettable = true;
        memeBSettable = true;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Random random = new Random();
        int indexOne = 0;
        int indexTwo = 0;

        while(indexOne == indexTwo){
            indexOne = random.nextInt(memeCount);
            indexTwo = random.nextInt(memeCount);
        }
        DatabaseReference fatherMeme = database.getReference("memes");
        fatherMeme.orderByKey().limitToLast(indexOne + 1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    if(memeASettable){
                        DatabaseReference firstMemeRef = dataSnapshot.getRef();
                        firstMemeRef.addListenerForSingleValueEvent(memeListenerA);
                        memeASettable = false;
                    }
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

        DatabaseReference motherMeme = database.getReference("memes");
        motherMeme.orderByKey().limitToLast(indexTwo + 1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if(memeBSettable){
                    DatabaseReference firstMemeRef = dataSnapshot.getRef();
                    firstMemeRef.addListenerForSingleValueEvent(memeListenerB);
                    memeBSettable = false;
                }
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
    ValueEventListener getMemeCount = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            memeCount = (int) dataSnapshot.getChildrenCount();
            setTwoMemes();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener memeListenerA = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

           memeA = (Meme) dataSnapshot.getValue(Meme.class);
           memeA.setId((String) dataSnapshot.getKey());
            Glide
                    .with(MemeActivity.this)
                    .load(memeA.getPictureId())
                    .into(memeAButton);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    ValueEventListener memeListenerB = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            memeB = dataSnapshot.getValue(Meme.class);
            memeB.setId(dataSnapshot.getKey());
            Glide
                    .with(MemeActivity.this)
                    .load(memeB.getPictureId())
                    .into(memeBButton);        }

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
        DatabaseReference referenceA = database.getReference("memes");
        DatabaseReference childRef = referenceA.child(memeA.getId());
        childRef.setValue(memeA);

        DatabaseReference referenceB = database.getReference("memes" );
        DatabaseReference childRefB = referenceA.child(memeB.getId());
        childRefB.setValue(memeB);
        updateCount();
    }

    private void goToProfile(){
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void loadMemes(){ //do not delete, loads memes for debug
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        DatabaseReference memes = rootRef.child("memes");

        for (int i = 0; i < 10; ++i){
            mStorageRef = FirebaseStorage.getInstance().getReference();
            //StorageReference memeRef = mStorageRef.child("images/rivers.jpg");

            String location = "gs://memesmash-f80c7.appspot.com/"+String.valueOf(i)+".jpg";
            DatabaseReference myRef = memes.push();
            myRef.setValue(new Meme("i", 1000, location));

        }
    }

}
