package com.example.ex11_storageapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex11_storageapp.file.FileInputActivity;
import com.example.ex11_storageapp.file.FileOutputActivity;
import com.example.ex11_storageapp.http0_webview.WebviewActivity;
import com.example.ex11_storageapp.http1.HttpActivity;
import com.example.ex11_storageapp.http2.FirstRetrofitActivity;
import com.example.ex11_storageapp.http3.RetrofitActivity;
import com.example.ex11_storageapp.sharedpreferences.MainActivity;


public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.button_0).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        findViewById(R.id.button_1).setOnClickListener(v -> {
            startActivity(new Intent(this, FileInputActivity.class));
        });
        findViewById(R.id.button_2).setOnClickListener(v -> {
            startActivity(new Intent(this, FileOutputActivity.class));
        });
        findViewById(R.id.button_3).setOnClickListener(v -> {
            startActivity(new Intent(this, com.example.ex11_storageapp.sqlite.MainActivity.class));
        });
        findViewById(R.id.button_4).setOnClickListener(v -> {
            startActivity(new Intent(this, WebviewActivity.class));
        });
        findViewById(R.id.button_5).setOnClickListener(v -> {
            startActivity(new Intent(this, HttpActivity.class));
        });
        findViewById(R.id.button_6).setOnClickListener(v -> {
            startActivity(new Intent(this, FirstRetrofitActivity.class));
        });

        findViewById(R.id.button_7).setOnClickListener(v -> {
            startActivity(new Intent(this, RetrofitActivity.class));
        });
    }
}
