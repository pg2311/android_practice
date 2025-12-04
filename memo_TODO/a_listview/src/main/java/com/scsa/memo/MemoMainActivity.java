package com.scsa.memo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.memo.databinding.ActivityMemoMainBinding;

public class MemoMainActivity extends AppCompatActivity {

    private ActivityMemoMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener( v -> {
            startActivity(new Intent(this, MemoEditActivity.class));
        });

        binding.btnInfo.setOnClickListener( v -> {
            startActivity(new Intent(this, MemoInfoActivity.class));
        });
    }
}