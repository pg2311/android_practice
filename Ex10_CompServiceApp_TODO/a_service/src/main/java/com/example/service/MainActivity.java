package com.example.service;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btn_start);
        Button btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(view -> {
            Intent intent = new Intent();
            ComponentName compName = new ComponentName(
                    "com.example.service",
                    "com.example.service.MyService"
            );

            intent.setComponent(compName);
            startService(intent);
        });

        btnStop.setOnClickListener(view -> {
            Intent intent = new Intent();
            ComponentName compName = new ComponentName(
                    "com.example.service",
                    "com.example.service.MyService"
            );

            intent.setComponent(compName);
            boolean isStop = stopService(intent);
            Log.d(TAG, "stopService::" + isStop);
        });
    }
}