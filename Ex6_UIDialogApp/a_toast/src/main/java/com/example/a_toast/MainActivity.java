package com.example.a_toast;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a_toast.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCallToast1.setOnClickListener(view -> {
            Toast toast = Toast.makeText(MainActivity.this,
                    "나 토스트야 ~",
                    Toast.LENGTH_LONG);
            toast.show();
        });

        binding.btnCallToast2.setOnClickListener(view -> {
            View layout = getLayoutInflater().inflate(R.layout.layout_toast, null);
            TextView textView = layout.findViewById(R.id.toast_text);

// 3. 찾은 TextView에 원하는 메시지를 설정합니다.
            String customMessage = "나 안드로이드야 ~~";
            textView.setText(customMessage);

            Toast toast = new Toast(getApplicationContext());
            // 토스트 View을 모양을 변경한다S
            toast.setView(layout);

            toast.setGravity(Gravity.CENTER, 100,0);
            toast.show();
        });
    }
}