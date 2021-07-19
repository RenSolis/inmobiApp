package com.example.inmobiapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiapp.R;
import com.example.inmobiapp.models.FavoriteAdapter;
import com.example.inmobiapp.models.Property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListFavorites extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FavoriteAdapter mFavoriteAdapter;
    ArrayList<Property> mFavoriteList;
    FirebaseFirestore database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_favorites);

        mRecyclerView = findViewById(R.id.listFavorites);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListFavorites.this));

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFavoriteList = new ArrayList<Property>();

        getFavorites();
    }

    public void getFavorites() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        database.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Property property = new Property();

                                property.setId(document.getId());
                                property.setAddress(document.get("address").toString());
                                property.setType(document.get("type").toString());
                                property.setPrice(Integer.parseInt(document.get("price").toString()));
                                property.setImage(document.get("image").toString());

                                mFavoriteList.add(property);
                            }

                            mFavoriteAdapter = new FavoriteAdapter(ListFavorites.this, mFavoriteList, new FavoriteAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    Intent intent = new Intent(ListFavorites.this, ShowProperty.class);
                                    intent.putExtra("property_key", property.getId());
                                    startActivity(intent);
                                }
                            });

                            mRecyclerView.setAdapter(mFavoriteAdapter);
                        } else {
                            Log.w("ERROR", task.getException());
                            Toast.makeText(ListFavorites.this, "No se pudo obtener sus favoritos", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }
}
