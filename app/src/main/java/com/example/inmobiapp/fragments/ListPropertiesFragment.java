package com.example.inmobiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiapp.R;
import com.example.inmobiapp.activities.ShowProperty;
import com.example.inmobiapp.models.Property;
import com.example.inmobiapp.models.PropertyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListPropertiesFragment extends Fragment {
    RecyclerView mRecyclerView;
    PropertyAdapter mPropertyAdapter;
    ArrayList<Property> mPropertyList;
    FirebaseFirestore database;

    public ListPropertiesFragment() {}

    public static ListPropertiesFragment newInstance() {
        ListPropertiesFragment fragment = new ListPropertiesFragment();

        // Bundle args = new Bundle();
        // args.putString("algo", param1);
        // args.putString("algo2", param2);
        // fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

                            mPropertyAdapter = new PropertyAdapter(getActivity(), mPropertyList, new PropertyAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_SHORT);

                                    Intent intent = new Intent(getActivity(), ShowProperty.class);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview_inmuebles, container, false);
    }
}
