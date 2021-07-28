package com.example.inmobiapp.fragments;

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
import androidx.fragment.app.FragmentTransaction;
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

public class ListFavoritesFragment extends Fragment {
    RecyclerView mRecyclerView;
    FavoriteAdapter mFavoriteAdapter;
    ArrayList<Property> mFavoriteList;
    FirebaseFirestore database;
    FirebaseAuth mAuth;

    public ListFavoritesFragment() {}

    public static ListFavoritesFragment newInstance() {
        return new ListFavoritesFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = getView().findViewById(R.id.text_fav);
        title.setText("Favoritos");

        mRecyclerView = getView().findViewById(R.id.listFavorites);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

                            mFavoriteAdapter = new FavoriteAdapter(getActivity(), mFavoriteList, new FavoriteAdapter.ItemClickListener() {
                                @Override
                                public void onItemClickListener(Property property) {
                                    PropertyShowFragment fragment = PropertyShowFragment.newInstance(property.getId());
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                                    transaction.replace(R.id.container, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });

                            mRecyclerView.setAdapter(mFavoriteAdapter);
                        } else {
                            Log.w("ERROR", task.getException());
                            Toast.makeText(getActivity(), "No se pudo obtener sus favoritos", Toast.LENGTH_SHORT);
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
