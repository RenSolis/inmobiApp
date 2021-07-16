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
import com.example.inmobiapp.models.Property;
import com.example.inmobiapp.models.PropertyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListProperties extends AppCompatActivity {
    RecyclerView mRecyclerView;
    PropertyAdapter mPropertyAdapter;
    ArrayList<Property> mPropertyList;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_inmuebles);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ListProperties.this));

        database = FirebaseFirestore.getInstance();

        mPropertyList = new ArrayList<Property>();

        getProperties();
    }

    private void getProperties() {
        database.collection("properties")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Property property = new Property();

                                property.setId(document.getId());
                                property.setAcquisition(document.get("acquisition").toString());
                                property.setType(document.get("type").toString());
                                property.setAddress(document.get("address").toString());
                                property.setImage(document.get("image").toString());

                                mPropertyList.add(property);
                            }

                            mPropertyAdapter = new PropertyAdapter(ListProperties.this, mPropertyList, new PropertyAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    Toast.makeText(ListProperties.this, "HOLA", Toast.LENGTH_SHORT);

                                    Intent intent = new Intent(ListProperties.this, ShowProperty.class);
                                    intent.putExtra("property_key", property.getId());
                                    startActivity(intent);
                                }
                            });

                            mRecyclerView.setAdapter(mPropertyAdapter);
                        } else {
                            Log.w("ERROR", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }
}
