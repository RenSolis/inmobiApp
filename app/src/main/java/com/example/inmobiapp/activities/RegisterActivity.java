package com.example.inmobiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText et_Name;
    EditText et_LastName;
    EditText et_Email;
    EditText et_Password;
    EditText et_PasswordConfirmation;
    Button b_Register;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_Name = findViewById(R.id.et_name);
        et_LastName = findViewById(R.id.et_lastname);
        et_Email = findViewById(R.id.et_email);
        et_Password = findViewById(R.id.et_password);
        et_PasswordConfirmation = findViewById(R.id.et_confirmpassword);
        b_Register = findViewById(R.id.register_button);

        database = FirebaseFirestore.getInstance();

        b_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String lastName;
                String email;
                String password;
                String passwordConfirmation;

                name = et_Name.getText().toString().trim();
                lastName = et_LastName.getText().toString().trim();
                email = et_Email.getText().toString().trim();
                password = et_Password.getText().toString().trim();
                passwordConfirmation = et_PasswordConfirmation.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    et_Name.setError("Nombre obligatorio");
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    et_LastName.setError("Apellidos obligatorios");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    et_Email.setError("Correo obligatorio");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_Email.setError("Correo inválido");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    et_Password.setError("Contraseña obligatoria");
                    return;
                }

                if (password.length() < 6){
                    et_Password.setError("Contraseña debe tener más de 6 caracteres");
                    return;
                }

                if (!passwordConfirmation.equals(password)){
                    et_PasswordConfirmation.setError("Contraseñas no coinciden");
                    return;
                }

                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("lastName", lastName);
                                user.put("email", email);

                                // TODO: validar como funciona cuando se vuelve a mandar el mismo correo del usuario
                                database.collection("users")
                                        .document(currentUser.getUid())
                                        .set(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(RegisterActivity.this, MainLogged.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.w("Error Login:", task.getException());
                                                    Toast.makeText(RegisterActivity.this, "No se pudo registrar al usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Log.w("ERROR:", task.getException());
                                Toast.makeText(RegisterActivity.this, "No se pudo crear la cuenta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }
}
