package com.scsa.ex5_1_uieventapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 이벤트 처리 절차
 * 1. 이벤트 소스 결정, 이벤트 종류 결정
 * 2. 이벤트 처리기(핸들러)
 * 3. 2)을 1)에 설정
 */

// 2. 이벤트 처리기
class MyHandler implements View.OnTouchListener {
    private static final String TAG = "MyHandler";

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        MotionEvent.ACTION_DOWN : 화면 터치 시작
//        MotionEvent.ACTION_MOVE : 터치 이동
//        MotionEvent.ACTION_UP : 터치 끝

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "화면터치시작 x=" + x + ", y=" + y);
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "화면터치 이동 x=" + x + ", y=" + y);
                return true;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "화면터치 끝 x=" + x + ", y=" + y);
                return true;
            default:
                Log.i(TAG, "Event: " + event.getAction());
                break;
        }
        return false;
    }
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);

        // 1. MainActivity 화면 전체를 이벤트 소스로 선택
        View view = new View(this);
        setContentView(view);

        // 3. 이벤트 소스에 이벤트 핸들러 연결
        view.setOnTouchListener(new MyHandler());

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}