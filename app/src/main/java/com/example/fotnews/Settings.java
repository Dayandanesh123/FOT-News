package com.example.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    private LinearLayout signoutConfirmLayout;
    private LinearLayout editProfileLayout;
    private LinearLayout userInfoLayout;
    private LinearLayout editInfoButton;
    private LinearLayout signoutButton;
    private Button btnSignoutCancel;
    private Button btnSignoutConfirm;
    private Button btnEditCancel;
    private Button btnEditSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        // Initialize views
        signoutConfirmLayout = findViewById(R.id.activity_signout_confirm);
        editProfileLayout = findViewById(R.id.activity_edit_profile);
        userInfoLayout = findViewById(R.id.user_info_layout);
        editInfoButton = findViewById(R.id.edit_info);
        signoutButton = findViewById(R.id.signout);
        btnSignoutCancel = findViewById(R.id.btn_signout_cancel);
        btnSignoutConfirm = findViewById(R.id.btn_signout_confirm);
        btnEditCancel = findViewById(R.id.btn_edit_cancel);
        btnEditSave = findViewById(R.id.btn_edit_save);

        // Set click listeners
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show edit profile layout and hide signout confirm if visible
                editProfileLayout.setVisibility(View.VISIBLE);
                signoutConfirmLayout.setVisibility(View.GONE);
                // Move user info down
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.activity_edit_profile;
                userInfoLayout.setLayoutParams(params);
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show signout confirm layout and hide edit profile if visible
                signoutConfirmLayout.setVisibility(View.VISIBLE);
                editProfileLayout.setVisibility(View.GONE);
                // Move user info down
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.activity_signout_confirm;
                userInfoLayout.setLayoutParams(params);
            }
        });

        btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide edit profile layout
                editProfileLayout.setVisibility(View.GONE);
                // Move user info back up
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.back;
                userInfoLayout.setLayoutParams(params);
            }
        });

        btnSignoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide signout confirm layout
                signoutConfirmLayout.setVisibility(View.GONE);
                // Move user info back up
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.back;
                userInfoLayout.setLayoutParams(params);
            }
        });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save logic here
                editProfileLayout.setVisibility(View.GONE);
                // Move user info back up
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.back;
                userInfoLayout.setLayoutParams(params);
            }
        });

        btnSignoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle sign out logic here
                signoutConfirmLayout.setVisibility(View.GONE);
                // Move user info back up
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) userInfoLayout.getLayoutParams();
                params.topToBottom = R.id.back;
                userInfoLayout.setLayoutParams(params);
                // Perform sign out
                performSignOut();
            }
        });
    }

    private void performSignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Main.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}