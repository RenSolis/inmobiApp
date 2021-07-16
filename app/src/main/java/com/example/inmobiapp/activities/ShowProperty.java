package com.example.inmobiapp.activities;

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
import com.google.firebase.firestore.DocumentReference;
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
    String mImageURL;
    Button mButtonFavorite;
    FirebaseFirestore database;
    DocumentReference users;
    String imgURL;

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
                                mType.setText(document.get("type").toString());
                                mMeters.setText(document.get("meters").toString());
                                mRooms.setText(document.get("rooms").toString());
                                mAddress.setText(document.get("address").toString());
                                mFloors.setText(document.get("floors").toString());
                                mPrice.setText(document.get("price").toString());
                                mAcquisition.setText(document.get("acquisition").toString());
                                Picasso.get().load(document.get("image").toString()).into(mImage);
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
                int meters = Integer.parseInt(mMeters.getText().toString());
                String type = mType.getText().toString();
                int rooms = Integer.parseInt(mRooms.getText().toString());
                String address = mAddress.getText().toString();
                int floors = Integer.parseInt(mFloors.getText().toString());
                int price = Integer.parseInt(mPrice.getText().toString());
                String acquisition = mAcquisition.getText().toString();

                Property property = new Property(meters, rooms, floors, price, type, address, acquisition, imgURL);
                database.collection("users")
                        .document(currentUser.getUid())
                        .collection("favorites")
                        .document()
                        .set(property);
            }
        });
    }
}
