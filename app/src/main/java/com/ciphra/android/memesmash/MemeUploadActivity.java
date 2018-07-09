package com.ciphra.android.memesmash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MemeUploadActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private String TAG = "meme_smash";
    private Meme thisMeme;
    private StorageReference memeRef;
    private DatabaseReference myRef;



    private int PICK_IMAGE = 733;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_upload);


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private void completeUpload(StorageReference downloadURL){

        downloadURL.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                thisMeme.setPictureId(uri.toString());
                myRef.setValue(thisMeme);
                Intent intent = new Intent(getApplicationContext(), MemeActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            Bitmap bmp = null;


            try {
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageBytes = stream.toByteArray();
                FirebaseStorage storage = FirebaseStorage.getInstance();


                //create meme

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference rootRef = database.getReference();
                DatabaseReference memes = rootRef.child("memes");
                mStorageRef = FirebaseStorage.getInstance().getReference();
                myRef = memes.push();
                thisMeme = new Meme("i", 1000, null);


                //upload image

                StorageReference storageRef =
                        storage.getReferenceFromUrl("gs://memesmash-f80c7.appspot.com");

                memeRef = storageRef.child(myRef.getKey());
                UploadTask uploadTask = memeRef.putBytes(imageBytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload failed!",
                                Toast.LENGTH_LONG).show();
                    }
                });

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Upload succeeded!",
                                Toast.LENGTH_LONG).show();
                        Log.i(TAG, taskSnapshot.getStorage().getPath());
                        completeUpload(taskSnapshot.getStorage());

                    }
                });




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
