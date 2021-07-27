package com.example.inmobiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiapp.R;
import com.example.inmobiapp.activities.ShowProperty;
import com.example.inmobiapp.models.FavoriteAdapter;
import com.example.inmobiapp.models.OwnerPropertyAdapter;
import com.example.inmobiapp.models.Property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOwnerPropertiesFragment extends Fragment {
    RecyclerView mRecyclerView;
    OwnerPropertyAdapter mOwnerPropertyAdapter;
    ArrayList<Property> mFavoriteList;
    FirebaseFirestore database;
    FirebaseAuth mAuth;

    public ListOwnerPropertiesFragment() {}

    public static ListOwnerPropertiesFragment newInstance() {
        return new ListOwnerPropertiesFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = getView().findViewById(R.id.text_fav);
        title.setText("Mis Inmuebles");

        mRecyclerView = getView().findViewById(R.id.listFavorites);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFavoriteList = new ArrayList<Property>();

        getOwnerProperties();
    }

    public void getOwnerProperties() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        database.collection("users")
                .document(currentUser.getUid())
                .collection("owner_properties")
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

                            mOwnerPropertyAdapter = new OwnerPropertyAdapter(getActivity(), mFavoriteList, new OwnerPropertyAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    Intent intent = new Intent(getActivity(), ShowProperty.class);
                                    intent.putExtra("property_key", property.getId());
                                    startActivity(intent);
                                }
                            });

                            mRecyclerView.setAdapter(mOwnerPropertyAdapter);
                        } else {
                            Log.w("ERROR", task.getException());
                            Toast.makeText(getActivity(), "No se pudo obtener sus inmuebles", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview_favorites, container, false);
    }
}
