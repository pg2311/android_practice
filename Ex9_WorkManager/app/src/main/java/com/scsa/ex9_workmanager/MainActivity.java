package com.scsa.ex9_workmanager;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * WorkManager
 * 1. 제약조건 설정
 * 2. Activity가 WorkManager에게 데이터를 전달 -> Worker가 처리
 * 3. 작업요청
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        2) 제약조건 설정
        Constraints constraints = new Constraints.Builder()
//                .setRequiresBatteryNotLow()
                .setRequiredNetworkType(NetworkType.CONNECTED) // 필수: 네트워크 연결
                .setRequiresCharging(true)                    // 선택: 충전 중일 것
                .setRequiresStorageNotLow(true)               // 선택: 저장 공간이 부족하지 않을 것
                .build();

//        3) 입력데이터 설정
        Data inputData = new Data.Builder()
                .putString("file1", "a.txt")
                .build();

//        4) 작업 요청 (OneTimeWorkRequest)하기 :  일회성 작업 요청
        OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints) // 1.에서 정의한 제약 조건 적용
                .setInputData(inputData)     // 2.에서 정의한 입력 데이터 적용
                .build();

//        5) WorkManager에게 작업 등록
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(uploadRequest);

        Log.d(TAG, "데이터 업로드 작업을 예약합니다.");
    }
}