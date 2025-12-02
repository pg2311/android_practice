package com.example.android.b_othertext;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.MultiAutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // 제안할 데이터 배열
        String[] suggestions = {"animal", "android", "cherry", "chair", "basket", "ant"};

        /* AutoCompleteTextView */
        // 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

        /* MultiAutoCompleteTextView */
        MultiAutoCompleteTextView multiAutoCompleteTextView = findViewById(R.id.multiAutoCompleteTextView);
        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        /* CheckedTextView listChoiceIndicatorMultiple*/
        CheckedTextView checkedTextView1 = findViewById(R.id.checkedTextView1);
        checkedTextView1.setOnClickListener(v -> {
            checkedTextView1.toggle(); // 체크 상태 전환
        });

        CheckedTextView checkedTextView2 = findViewById(R.id.checkedTextView2);
        checkedTextView2.setChecked(false); // 초기 체크 상태 설정
        checkedTextView2.setOnClickListener(v -> {
            checkedTextView2.toggle();
        });

        /* */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}