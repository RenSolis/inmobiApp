package com.example.inmobiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.inmobiapp.R;
import com.example.inmobiapp.models.Property;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PropertyShowFragment extends Fragment {
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

    public PropertyShowFragment() {}

    public static PropertyShowFragment newInstance(String property_id) {
        PropertyShowFragment fragment = new PropertyShowFragment();
        Bundle args = new Bundle();
        args.putString("property_id", property_id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImage = getView().findViewById(R.id.detail_Img);
        mMeters = getView().findViewById(R.id.detail_TVMeters);
        mRooms = getView().findViewById(R.id.detail_TVRooms);
        mAddress = getView().findViewById(R.id.detail_TVAddress);
        mFloors = getView().findViewById(R.id.detail_TVFloors);
        mPrice = getView().findViewById(R.id.detail_TVPrice);
        mAcquisition = getView().findViewById(R.id.detail_TVAdq);
        mType = getView().findViewById(R.id.detail_TVType);
        mButtonFavorite = getView().findViewById(R.id.detail_BttnFavorite);

        if (getArguments() == null) {
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String keyId = getArguments().getString("property_id");

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
                            Toast.makeText(getActivity(), "Ocurrio un error!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getActivity(), "Se añadió a favoritos", Toast.LENGTH_SHORT).show();

                                    ListFavoritesFragment fragment = ListFavoritesFragment.newInstance();
                                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                                    transaction.replace(R.id.container, fragment);
                                    transaction.addToBackStack(null);
                                    Log.d("HUEVADAS", "funciono!A");
                                    transaction.commit();
                                } else {
                                    Log.w("ERROR:", task.getException());
                                    Toast.makeText(getActivity(), "Hubo un error al agregar en favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_detail, container, false);
    }
}
