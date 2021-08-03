package com.example.inmobiapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.inmobiapp.R;
import com.example.inmobiapp.fragments.ListFavoritesFragment;
import com.example.inmobiapp.fragments.ListOwnerPropertiesFragment;
import com.example.inmobiapp.fragments.ListPropertiesFragment;
import com.example.inmobiapp.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainLogged extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_logged_layout);

        bottomNavigation = findViewById(R.id.navigationBar);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        openFragment(ListPropertiesFragment.newInstance());
    }

    //Inicializamos el fragmento que contendrá el listado
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //Establecemos la navegación dentro de la página. Permitirá redirigir hacia las disitintas pantallas.
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    openFragment(ListPropertiesFragment.newInstance()); //Hacia el home.
                    return true;
                case R.id.navigation_favorites:
                    openFragment(ListFavoritesFragment.newInstance()); //Hacia la pantalla de favoritos.
                    return true;
                case R.id.navigation_owner_properties:
                    openFragment(ListOwnerPropertiesFragment.newInstance()); //Hacia la pantalla de mis propiedades.
                    return true;
                case R.id.navigation_profile:
                    openFragment(UserProfileFragment.newInstance()); //Hacia la pantalla de mi perfil.
                    return true;
            }

            return false;
        }
    };
}
