package com.example.ex1_helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Log.i(TAG, "전달된 extra name=" + name);
        Toast.makeText(this, "전달된 extra name=" + name, Toast.LENGTH_LONG).show();

        Button btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(v -> {
            Intent backIntent = new Intent();
            backIntent.putExtra("resultKey", "Message From NextActivity");
            setResult(NextActivity.RESULT_OK, backIntent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
