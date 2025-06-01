package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputEditText emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private LinearLayout signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);

        firebaseAuth = FirebaseAuth.getInstance();
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.email);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password1);
        confirmPasswordInput = findViewById(R.id.password2);
        signupButton = findViewById(R.id.back3);
        loginRedirectText = findViewById(R.id.signin);
    }

    private void setupClickListeners() {
        signupButton.setOnClickListener(v -> handleRegistration());
        loginRedirectText.setOnClickListener(v -> redirectToLogin());
    }

    private void handleRegistration() {
        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (!validateInputs(email, username, password, confirmPassword)) {
            return;
        }

        createFirebaseUser(email, password, username);
    }

    private boolean validateInputs(String email, String username, String password, String confirmPassword) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            emailInput.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            usernameInput.setError("Enter username");
            usernameInput.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password should be at least 6 characters");
            passwordInput.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            confirmPasswordInput.requestFocus();
            return false;
        }

        return true;
    }

    private void createFirebaseUser(String email, String password, String username) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUserProfile(username);
                    } else {
                        showError("Error: " + task.getException().getMessage());
                    }
                });
    }

    private void updateUserProfile(String username) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserToFirestore(user, username);
                    } else {
                        showError("Failed to set username");
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", user.getUid());
        userData.put("username", username);

        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    showSuccess("Account created successfully!");
                    redirectToLogin();
                })
                .addOnFailureListener(e -> showError("Failed to save user data"));
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}