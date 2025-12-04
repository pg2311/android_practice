package com.example.ex7_compactivityapp.a_lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next);

        //전달된 Intent얻기
        Intent intent = getIntent(); // NextActivity를 시작시킨 Intent 객체를 가져옵니다.
        String action = intent.getAction();  //인텐트에 설정된 액션(ACTION_SEND)과 타입(text/plain)을 가져옵니다.
        String type = intent.getType();

        // 1. Action이 Intent.ACTION_SEND이고 Type이 text/plain인지 확인
        if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {

            // 2. Intent.EXTRA_TEXT로 전달된 텍스트 데이터 추출
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT); //Intent.EXTRA_TEXT

            if (sharedText != null) {
                // 3. 추출된 데이터를 화면에 표시하거나 처리
                TextView sharedTextView = findViewById(R.id.shared_text_view);
                sharedTextView.setText("수신된 공유 내용: \n" + sharedText);

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.next), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d(TAG, "onCreate() - NextActivity");

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() - NextActivity");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() - NextActivity");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart() - NextActivity");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() - NextActivity");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() - NextActivity");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() - NextActivity");
        super.onDestroy();
    }
}