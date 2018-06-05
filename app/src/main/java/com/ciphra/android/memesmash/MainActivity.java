package com.ciphra.android.memesmash;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = "meme_smash";

    private Button registerButton;
    private Button loginButton;
    private Button submitEmail;
    private Button cancelEmail;
    private Button submitLoginEmail;
    private Button cancelLoginEmail;
    private Button registerWithEmail;
    private Button registerWithGoogle;
    private Button registerWithFacebook;
    private Button cancelRegistration;
    private FirebaseUser user;

    private EditText emailEditText;
    private EditText emailPasswordEditText;
    private EditText emailLoginEditText;
    private EditText emailPasswordLoginEditText;
    private EditText emailPasswordConfirmBox;
    public static final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(Object currentUser){ //TOOOODOOOOO
        if(currentUser == null){
            inflateRegistration();
        }
        else{
            Toast.makeText(MainActivity.this, "We did it!.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void inflateRegistration(){
        setContentView(R.layout.signup_login);
        registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateRegistrationPicker();
            }
        });
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateEmailLogin();
            }
        });
    }

    private void inflateEmailRegistration(){
        setContentView(R.layout.email_register_form);
        cancelEmail = (Button) findViewById(R.id.cancel_email_registration);
        cancelEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(null);
            }
        });

        submitEmail = (Button) findViewById(R.id.submit_email_registration);
        submitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText = (EditText) findViewById(R.id.email_registration_box);
                emailPasswordEditText = (EditText) findViewById(R.id.email_password_box);
                emailPasswordConfirmBox = (EditText) findViewById(R.id.email_password_confirm_box);

                if(!emailPasswordEditText.getText().toString().equals(emailPasswordConfirmBox.getText().toString())){
                    Toast.makeText(MainActivity.this, "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    emailRegistration(emailEditText.getText().toString(), emailPasswordEditText.getText().toString());
                }

            }
        });
    }

    private void emailRegistration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            mAuth.signOut();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("Registration successful.  Follow the link emailed to you to verify your account.");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            updateUI(null);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
    }

    private void emailLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();

                            Log.d(TAG, "signInWithEmail:success");
                            if(!user.isEmailVerified()){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                builder1.setMessage("You must verify your email address before you can login.");
                                builder1.setPositiveButton(R.string.resend, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();

                                        FirebaseUser user  = mAuth.getCurrentUser();

                                        user.sendEmailVerification();

                                        mAuth.signOut();
                                        user = mAuth.getCurrentUser();
                                        updateUI(user);


                                    }
                                });
                                builder1.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAuth.signOut();
                                        user = mAuth.getCurrentUser();
                                    }
                                });
                                builder1.setCancelable(true);
                                builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        mAuth.signOut();
                                        user = mAuth.getCurrentUser();
                                    }
                                });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            else{
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void inflateEmailLogin(){
        setContentView(R.layout.email_login);
        cancelLoginEmail = (Button) findViewById(R.id.login_email_cancel);
        cancelLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(null);
            }
        });
        submitLoginEmail = (Button) findViewById(R.id.login_email_submit);
        submitLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLoginEditText = (EditText) findViewById(R.id.email_box_login);
                emailPasswordLoginEditText = (EditText) findViewById(R.id.email_password_box_login);
                emailLogin(emailLoginEditText.getText().toString(), emailPasswordLoginEditText.getText().toString());
            }
        });
    }

    private void inflateRegistrationPicker(){
        setContentView(R.layout.registration_picker);

        registerWithEmail = (Button) findViewById(R.id.register_with_email_button);
        registerWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateEmailLogin();
            }
        });

        cancelRegistration = (Button) findViewById(R.id.cancel_registration);
        cancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(null);
            }
        });

        registerWithGoogle = (Button) findViewById(R.id.register_with_google_button);
        registerWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerWithGoogle();
            }
        });


    }


    private void registerWithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        updateUI(account);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout),
                            // "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
