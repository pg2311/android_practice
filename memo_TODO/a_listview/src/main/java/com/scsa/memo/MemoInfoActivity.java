package com.scsa.memo;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.scsa.memo.databinding.ActivityMemoInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class MemoInfoActivity extends AppCompatActivity {

    private ActivityMemoInfoBinding binding;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMemoInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//TODO 2. 데이터를 ArrayAdapter에 등록하고 listView에 Adapt합니다
        list.add("메모앱 만들기1 2025-12-2 ");
        list.add("메모앱 만들기2 2025-12-3 ");
        list.add("메모앱 만들기3 2025-12-4 ");

//    ---------------------------------------
// Resource: ListView의 각 항목에 사용할 레이아웃 리소스 ID
// Data: ListView에 표시할 데이터 리스트
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, //Android 시스템이 미리 정의해 둔 TextView하나만 포함하는 표준 레이아웃
                list
        );

//        ListView listView = findViewById(R.id.list_view);
//        listView.setAdapter(arrayAdapter);
//        binding.listView.setAdapter(arrayAdapter);
//    ---------------------------------------

        // TODO 3. 데이터를 MemoAdapter에 등록하고 listView에 Adapt합니다
        MemoAdapter memoAdapter = new MemoAdapter(list);
        binding.listView.setAdapter(memoAdapter);

        binding.back.setOnClickListener(v -> {
            finish();
        });
    }
}

