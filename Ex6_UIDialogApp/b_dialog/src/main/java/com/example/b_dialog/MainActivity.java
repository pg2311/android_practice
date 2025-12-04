package com.example.b_dialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_0).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DialogActivity.class)));
        findViewById(R.id.button_1).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, DatePickerActivity.class)));
        findViewById(R.id.button_2).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TimePickerActivity.class)));

    }
}