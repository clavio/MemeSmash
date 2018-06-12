package com.ciphra.android.memesmash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class RegistrationPickerActivity extends AppCompatActivity {

    private Button mGoogleButton;
    private Button mEmailButton;
    private Button mCancelButton;
    private LoginButton registerWithFacebook;

    private String TAG = "meme_smash";
    private static final String EMAIL = "email";

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_picker);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();


        registerWithFacebook = findViewById(R.id.facebook_button);
        registerWithFacebook.setReadPermissions(Arrays.asList(EMAIL));
        registerWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.i("fsadafsd", "fafsda");
            }

            @Override
            public void onCancel() {
                Toast.makeText(RegistrationPickerActivity.this, "Registration Failed!",
                        Toast.LENGTH_SHORT).show();
                returnToMain();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegistrationPickerActivity.this, "Registration Failed!",
                        Toast.LENGTH_SHORT).show();
                returnToMain();
            }
        });


        mCancelButton = findViewById(R.id.cancel_registration);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            returnToMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationPickerActivity.this, "Registration Failed!",
                                    Toast.LENGTH_SHORT).show();
                            returnToMain();
                        }
                    }
                });
    }

    private void returnToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
