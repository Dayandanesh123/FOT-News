package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initializeAuthAndRedirect();
    }

    private void initializeAuthAndRedirect() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            redirectToLoginScreen();
        } else {
            redirectToHomeScreen();
        }
    }

    private void redirectToLoginScreen() {
        startActivity(new Intent(Main.this, Login.class));
        finish();
    }

    private void redirectToHomeScreen() {
        startActivity(new Intent(Main.this, Home.class));
        finish();
    }
}