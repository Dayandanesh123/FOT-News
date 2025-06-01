package com.example.fotnews;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.LinearLayout;

public class Dev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dev_info);

        findViewById(R.id.back3).setOnClickListener(v -> finish());
    }
}
