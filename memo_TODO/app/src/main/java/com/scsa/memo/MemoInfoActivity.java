package com.scsa.memo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.memo.databinding.ActivityMemoInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class MemoInfoActivity extends AppCompatActivity {

    private ActivityMemoInfoBinding binding;
    private List<String> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(v -> {
            finish();
        });
    }
}

