package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class Settings extends AppCompatActivity {

    // UI Components
    private LinearLayout signoutConfirmLayout;
    private LinearLayout DeleteProfileLayout;
    private LinearLayout editProfileLayout;
    private LinearLayout userInfoLayout;
    private LinearLayout editInfoButton;
    private LinearLayout signoutButton;
    private TextView emailTextView;
    private TextView usernameTextView;
    private EditText editUsernameInput;
    private LinearLayout btnSignoutCancel;
    private LinearLayout btnSignoutConfirm;
    private LinearLayout btnEditCancel;
    private LinearLayout btnEditSave;
    private ImageView backImageView;
    private TextView deleteProfile;
    private LinearLayout btnDelete;
    private LinearLayout btnkeep;

    // Firebase
    private FirebaseAuth auth;
    private ListenerRegistration userInfoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        backImageView = findViewById(R.id.back);
        backImageView.setOnClickListener(v -> finish());


        initializeViews();
        setupClickListeners();
        initializeFirebase();
        displayUserInfo();
        findViewById(R.id.dev).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, Dev.class);
            startActivity(intent);
        });

    }

    private void initializeViews() {
        signoutConfirmLayout = findViewById(R.id.activity_signout_confirm);
        editProfileLayout = findViewById(R.id.activity_edit_profile);
        userInfoLayout = findViewById(R.id.user_info_layout);
        editInfoButton = findViewById(R.id.edit_info);
        signoutButton = findViewById(R.id.signout);
        emailTextView = findViewById(R.id.email2);
        usernameTextView = findViewById(R.id.username1);
        editUsernameInput = findViewById(R.id.editUsername);
        btnSignoutCancel = findViewById(R.id.btnCancel);
        btnSignoutConfirm = findViewById(R.id.btnYes);
        btnEditCancel = findViewById(R.id.btn_edit_deny);
        btnEditSave = findViewById(R.id.btn_edit_Confirm);
        DeleteProfileLayout = findViewById(R.id.activity_delete_profile_confirm);
        deleteProfile = findViewById(R.id.delete);
        btnDelete = findViewById(R.id.btnDelete);
        btnkeep = findViewById(R.id.btnKeep);


    }

    private void initializeFirebase() {
        auth = FirebaseAuth.getInstance();
    }

    private void displayUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            emailTextView.setText("Email: " + (user.getEmail() != null ? user.getEmail() : ""));
            usernameTextView.setText("Loading username...");
        } else {
            emailTextView.setText("Not logged in");
            usernameTextView.setText("-");
        }
    }

    private void setupClickListeners() {
        editInfoButton.setOnClickListener(v -> showEditProfile());
        signoutButton.setOnClickListener(v -> showSignoutConfirmation());
        btnEditCancel.setOnClickListener(v -> hideEditProfile());
        btnSignoutCancel.setOnClickListener(v -> hideSignoutConfirmation());
        btnEditSave.setOnClickListener(v -> saveProfileChanges());
        btnSignoutConfirm.setOnClickListener(v -> performSignOut());
        deleteProfile.setOnClickListener(v -> showDeleteProfile());
        btnDelete.setOnClickListener(v -> preformDeleteProfile());
        btnkeep.setOnClickListener(v -> hideDeleteProfile());

    }
    private void showDeleteProfile() {
        DeleteProfileLayout.setVisibility(View.VISIBLE);
        editProfileLayout.setVisibility(View.GONE);
        signoutConfirmLayout.setVisibility(View.GONE);
        updateUserInfoLayoutPosition(R.id.activity_delete_profile_confirm);
    }


    private void showEditProfile() {
        editProfileLayout.setVisibility(View.VISIBLE);
        signoutConfirmLayout.setVisibility(View.GONE);
        DeleteProfileLayout.setVisibility(View.GONE);
        updateUserInfoLayoutPosition(R.id.activity_edit_profile);
    }

    private void showSignoutConfirmation() {
        signoutConfirmLayout.setVisibility(View.VISIBLE);
        editProfileLayout.setVisibility(View.GONE);
        DeleteProfileLayout.setVisibility(View.GONE);
        updateUserInfoLayoutPosition(R.id.activity_signout_confirm);
    }

    private void hideDeleteProfile() {
        DeleteProfileLayout.setVisibility(View.GONE);
        resetUserInfoLayoutPosition();
    }
    private void hideEditProfile() {
        editProfileLayout.setVisibility(View.GONE);
        resetUserInfoLayoutPosition();
    }

    private void hideSignoutConfirmation() {
        signoutConfirmLayout.setVisibility(View.GONE);
        resetUserInfoLayoutPosition();
    }

    private void preformDeleteProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show();

                            // Redirect to login screen or exit app
                            Intent intent = new Intent(this, Login.class); // change if needed
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthRecentLoginRequiredException) {
                                Toast.makeText(this, "Please re-login to delete your profile", Toast.LENGTH_LONG).show();
                                // Optional: You could redirect to re-login here
                            } else {
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveProfileChanges() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String newUsername = editUsernameInput.getText().toString().trim();
            if (newUsername.isEmpty()) {
                showToast("Username cannot be empty");
                return;
            }

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .update("username", newUsername)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Username updated");
                        hideEditProfile();
                    })
                    .addOnFailureListener(e -> {
                        showToast("Failed to update username");
                    });
        }
    }

    private void updateUserInfoLayoutPosition(int anchorViewId) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
        params.topToBottom = anchorViewId;
        userInfoLayout.setLayoutParams(params);
    }

    private void resetUserInfoLayoutPosition() {
        updateUserInfoLayoutPosition(R.id.back);
    }

    private void performSignOut() {
        auth.signOut();
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupUserInfoListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeUserInfoListener();
    }

    private void setupUserInfoListener() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        userInfoListener = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .addSnapshotListener(this::handleUserInfoUpdate);
    }

    private void handleUserInfoUpdate(DocumentSnapshot documentSnapshot, Throwable error) {
        if (error != null) {
            showToast("Failed to load user info");
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                String username = documentSnapshot.getString("username");

                emailTextView.setText("Email: " + (email != null ? email : "-"));
                usernameTextView.setText("Username: " + (username != null ? username : "-"));
                editUsernameInput.setText(username != null ? username : ""); // for edit field
            }
        }
    }

    private void removeUserInfoListener() {
        if (userInfoListener != null) {
            userInfoListener.remove();
            userInfoListener = null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
