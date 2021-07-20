package com.example.inmobiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inmobiapp.R;
import com.example.inmobiapp.models.Property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ShowProperty extends AppCompatActivity {
    ImageView mImage;
    TextView mMeters;
    TextView mRooms;
    TextView mAddress;
    TextView mFloors;
    TextView mPrice;
    TextView mType;
    TextView mAcquisition;
    Button mButtonFavorite;
    FirebaseFirestore database;
    String imgURL;
    Property property;
    // TODO: crear un property donde se almacenen al obtenerlo todos los attributes y a partir de eso reutilizar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mImage = findViewById(R.id.detail_Img);
        mMeters = findViewById(R.id.detail_TVMeters);
        mRooms = findViewById(R.id.detail_TVRooms);
        mAddress = findViewById(R.id.detail_TVAddress);
        mFloors = findViewById(R.id.detail_TVFloors);
        mPrice = findViewById(R.id.detail_TVPrice);
        mAcquisition = findViewById(R.id.detail_TVAdq);
        mType = findViewById(R.id.detail_TVType);
        mButtonFavorite = findViewById(R.id.detail_BttnFavorite);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String keyId = getIntent().getStringExtra("property_key");

        database = FirebaseFirestore.getInstance();
        database.collection("properties")
                .document(keyId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String meters = document.get("meters").toString();
                                String rooms = document.get("rooms").toString();
                                String floors = document.get("floors").toString();
                                String price = document.get("price").toString();
                                String type = document.get("type").toString();
                                String address = document.get("address").toString();
                                String acquisition = document.get("acquisition").toString();
                                String image = document.get("image").toString();

                                property = new Property(
                                        Integer.parseInt(meters),
                                        Integer.parseInt(rooms),
                                        Integer.parseInt(floors),
                                        Integer.parseInt(price),
                                        type,
                                        address,
                                        acquisition,
                                        image
                                );
                                property.setId(document.getId());

                                mType.setText(type);
                                mMeters.setText(meters);
                                mRooms.setText(rooms);
                                mAddress.setText(address);
                                mFloors.setText(floors);
                                mPrice.setText(price);
                                mAcquisition.setText(acquisition);
                                Picasso.get().load(image).into(mImage);
                            }
                        } else {
                            Log.w("ERROR:", task.getException());
                            Toast.makeText(ShowProperty.this, "Ocurrio un error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.collection("users")
                        .document(currentUser.getUid())
                        .collection("favorites")
                        .document(property.getId())
                        .set(property)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ShowProperty.this, "Se añadió a favoritos", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ShowProperty.this, ListFavorites.class);
                                    startActivity(intent);
                                } else {
                                    Log.w("ERROR:", task.getException());
                                    Toast.makeText(ShowProperty.this, "Hubo un error al agregar en favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
