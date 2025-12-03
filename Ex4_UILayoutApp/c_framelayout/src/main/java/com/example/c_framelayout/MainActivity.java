package com.example.c_framelayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.c_framelayout.R;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_framelayout);

        ImageView iv01 = findViewById(R.id.imageView01);
        ImageView iv02 = findViewById(R.id.imageView02);
        Button btnChangeImage = findViewById(R.id.btnChangeImage);
        /*
         * TODO 1. 버튼이 클릭될 때마다 이미지를 변환합니다 R.id.imageView01<->02
         *  hint : v.getVisibility()
         */
        btnChangeImage.setOnClickListener(v -> {
            if (iv01.getVisibility() == View.VISIBLE) {
                iv01.setVisibility(View.INVISIBLE);  // CSS의 visibility: hidden과 같다.
//                iv01.setVisibility(View.GONE); // CSS의 display: none과 같다. 영역까지 사라짐.
                iv02.setVisibility(View.VISIBLE);
            } else if (iv02.getVisibility() == View.VISIBLE) {
                iv01.setVisibility(View.VISIBLE);
                iv02.setVisibility(View.INVISIBLE);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}