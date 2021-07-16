package com.example.inmobiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView mTv_Register;
    TextView mTv_ForgotPassword;
    Button mButton_Login;
    ImageButton mIb_GoogleLogin;
    EditText mEt_Email;
    EditText mEt_Password;
    FirebaseAuth mFirebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore database;
    static final int RC_SIGN_IN = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String accountToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(accountToken,null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", currentUser.getDisplayName());
                        user.put("email", currentUser.getEmail());

                        // TODO: validar que ya exista para no crear de mas
                        database.collection("users")
                                .document(currentUser.getUid())
                                .set(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,"Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(LoginActivity.this, ListProperties.class);
                                            startActivity(intent);
                                        } else {
                                            Log.w("Error Login:", task.getException());
                                            Toast.makeText(LoginActivity.this, "No se iniciar sesion :(", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Ups, algo salio mal: "+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTv_Register = findViewById(R.id.goto_signup);
        mTv_ForgotPassword = findViewById(R.id.tv_forgotpassword);
        mButton_Login = findViewById(R.id.login_button);
        mIb_GoogleLogin = findViewById(R.id.google_acc);
        mEt_Email = findViewById(R.id.login_email);
        mEt_Password = findViewById(R.id.login_password);

        mFirebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mTv_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "En proceso de creación", Toast.LENGTH_SHORT).show();
            }
        });

        mIb_GoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });

        mTv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mButton_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = mEt_Email.getText().toString().trim();
                String user_password = mEt_Password.getText().toString().trim();

                if (TextUtils.isEmpty(user_email)) {
                    mEt_Email.setError("Correo es obligatorio");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                    mEt_Email.setError("Ingrese un correo válido");
                    return;
                }

                if (TextUtils.isEmpty(user_password)) {
                    mEt_Password.setError("Contraseña es obligatoria");
                    return;
                }

                if (user_password.length() < 6) {
                    mEt_Password.setError("Contraseña debe tener mas de 6 caracteres");
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword(user_email,user_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, ListProperties.class);
                                    startActivity(intent);
                                } else {
                                    Log.w("Error Login:", task.getException());
                                    Toast.makeText(LoginActivity.this, "Correo/Contraseña érroneas", Toast.LENGTH_SHORT).show();
                                }
                            }
                });
            }
        });
    }
}
