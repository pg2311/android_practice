package com.example.ex1_helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity - 사용자 인터페이스 화면을 의미
 * extends Activity - 액션바를 지원하지 않음
 * extends AppCompatActivity - 액션바(또는 툴바)를 지원
 */
public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * Activity 라이프 사이클의 초기화역할을 하는 메서드
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

//        TextView tv = new TextView(this);
//        tv.setText("자바 코드로 만든 TextView");
//        setContentView(tv);

        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textView);
        Button bt = findViewById(R.id.button2);

        tv.setText("첫번째 TextView");
        bt.setText("첫번째 Button");

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), //contract
                result -> {                                           //callback
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        // 결과 처리
                        String toastMsg = "다시호출되기! NextActivity가 보내준 데이터 :" + data.getStringExtra("resultKey");
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    }
                }
        );

        bt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NextActivity.class);
            intent.putExtra("name", "myName");
            // 단방향 데이터 전달
//            startActivity(intent);

            // 양방향 데이터 전달
            activityResultLauncher.launch(intent);


        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}