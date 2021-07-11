package com.example.inmobiapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        statusTextView = (TextView) findViewById(R.id.status_text_view);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);
    }

    // @Override
    // public void onStart() {
        // super.onStart();

        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // hacer algo con el usuario actual
    // }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    public void signIn() {
        ActivityResultLauncher<Intent> signInActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == RC_SIGN_IN) {
                            Intent data = result.getData();
                            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                            handleSignInResult(signInResult);
                        }
                    }
                });

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // startActivityForResult(signInIntent, RC_SIGN_IN);
        signInActivityResult.launch(signInIntent);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handlerSignInResult: " + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            statusTextView.setText("Hello, " + account.getDisplayName());
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        statusTextView.setText("Signed Out");
    }
}