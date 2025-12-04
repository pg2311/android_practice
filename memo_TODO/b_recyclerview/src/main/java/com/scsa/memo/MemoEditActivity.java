package com.scsa.memo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.memo.databinding.ActivityMemoEditBinding;

public class MemoEditActivity extends AppCompatActivity {

    private ActivityMemoEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cancel.setOnClickListener( v -> {
            finish();
        });
    }
}