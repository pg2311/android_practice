package com.scsa.ex9_workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    private static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String fileData = data.getString("file1");

        // 업로드 작업 시뮬레이션
        Log.i(TAG, "업로드 작업 시작");
        try {
            Thread.sleep(5000);
            Log.i(TAG, "업로드 작업 완료");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "업로드 작업 실패", e);
            return Result.failure();
        }
    }
}
