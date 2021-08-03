package com.example.inmobiapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.inmobiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment {
    TextView mUserName;
    TextView mUserEmail;

    public UserProfileFragment() {}

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserName = (TextView) getView().findViewById(R.id.user_name);
        mUserEmail = (TextView) getView().findViewById(R.id.user_email);

        mUserName.setText(currentUser.getDisplayName());
        mUserEmail.setText(currentUser.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile, container, false);
    }
}
