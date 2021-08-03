package com.example.inmobiapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListPropertiesFragment extends Fragment implements SearchView.OnQueryTextListener {
    RecyclerView mRecyclerView;
    PropertyAdapter mPropertyAdapter;
    ArrayList<Property> mPropertyList;
    private ArrayList<Property> mPropertyListSearch;
    FirebaseFirestore database;
    private SearchView mSearchView;

    public ListPropertiesFragment() {}

    public static ListPropertiesFragment newInstance() {
        ListPropertiesFragment fragment = new ListPropertiesFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSearchView = (SearchView) getView().findViewById(R.id.inSearch);
        initListener();


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

                            mPropertyListSearch.addAll(mPropertyList);

                            mPropertyAdapter = new PropertyAdapter(getActivity(), mPropertyList, new PropertyAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_SHORT);

                                    PropertyShowFragment fragment = PropertyShowFragment.newInstance(property.getId());
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                                    transaction.replace(R.id.container, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
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

    public void filter(String strSearch){
        if (strSearch.length() == 0){
            mPropertyListSearch.clear();
            mPropertyListSearch.addAll(mPropertyList);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mPropertyListSearch.clear();
                List collect = mPropertyList.stream()
                        .filter(i->i.getAddress().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                mPropertyListSearch.addAll(collect);
            }else{
                mPropertyListSearch.clear();
                for (Property i : mPropertyList){
                    if(i.getAddress().toLowerCase().contains(strSearch)){
                        mPropertyListSearch.add(i);
                    }
                }
            }
        }
    }

    private void initListener() {
        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filter(s);
        return false;
    }
}
