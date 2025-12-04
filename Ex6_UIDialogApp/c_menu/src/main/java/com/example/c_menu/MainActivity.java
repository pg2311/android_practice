package com.example.c_menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;


public class MainActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 뷰 참조
        toolbar = findViewById(R.id.toolbar);
        // 툴바 설정
        setSupportActionBar(toolbar);

        findViewById(R.id.btn_1).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MenuXMLActivity.class));
        });

        findViewById(R.id.btn_2).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, MenuJavaActivity.class));
        });
    }
}