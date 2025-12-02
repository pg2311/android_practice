package com.example.g_viewbinding;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.g_viewbinding.databinding.ActivityMainBinding;
import com.example.g_viewbinding.databinding.ItemRowBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding binding = null;
//      TODO 1. activity_main.xml뷰바인딩하기
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.editTextText1.setText("코드로 조작!");
        ConstraintLayout rootView = binding.getRoot();
        setContentView(rootView);
//      TODO 2.
//        1) item_row.xml 뷰바인딩해서  rowBinding변수에 대입합니다
//          내부 뷰id(prodImg, prodName, prodPrice)에 적절한 값을 설정합니다
        ItemRowBinding rowBinding = null;
        rowBinding = ItemRowBinding.inflate(getLayoutInflater());
//        2) rowBinding객체의 내부 뷰에 접근하여 값을 설정합니다
        rowBinding.prodImg.setImageResource(R.drawable.c0002);
        rowBinding.prodName.setText("NAME2");
        rowBinding.prodPrice.setText("PRICE2");

//        3) rowBinding객체의 root를 MainActivityBiding에 추가합니다
        binding.container.addView(rowBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}