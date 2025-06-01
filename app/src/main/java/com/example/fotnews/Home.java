package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Home extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private LinearLayout newsContainer;
    private ImageView profileAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        initializeFirebase();
        initializeViews();
        setupClickListeners();
        loadNewsArticles();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();
    }

    private void initializeViews() {
        newsContainer = findViewById(R.id.newsContainer);
        profileAvatar = findViewById(R.id.profileAvatar);
    }

    private void setupClickListeners() {
        profileAvatar.setOnClickListener(v -> navigateToSettings());
    }

    private void loadNewsArticles() {
        firestore.collection("news")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        displayNewsArticle(document);
                    }
                })
                .addOnFailureListener(e ->
                        showToast("Failed to load news"));
    }

    private void displayNewsArticle(QueryDocumentSnapshot document) {
        String title = document.getString("title");
        String date = document.getString("date");
        String content = document.getString("content");
        String imageUrl = document.getString("imageUrl");

        createNewsCard(title, date, content, imageUrl);
    }

    private void createNewsCard(String title, String date, String content, String imageUrl) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.card, newsContainer, false);

        TextView titleView = cardView.findViewById(R.id.newsTitle);
        TextView dateView = cardView.findViewById(R.id.newsDate);
        TextView contentView = cardView.findViewById(R.id.newsContent);
        ImageView imageView = cardView.findViewById(R.id.imageView);

        titleView.setText(title);
        dateView.setText(date);
        contentView.setText(content);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }

        newsContainer.addView(cardView);
    }

    private void navigateToSettings() {
        startActivity(new Intent(this, Settings.class));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}