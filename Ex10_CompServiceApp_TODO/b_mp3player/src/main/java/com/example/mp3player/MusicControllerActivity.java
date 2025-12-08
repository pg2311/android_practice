package com.example.mp3player;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mp3player.databinding.ActivityMusicControllerBinding;


public class MusicControllerActivity extends AppCompatActivity {

    private static final String TAG = "MusicControllerActivity";
    private ActivityMusicControllerBinding binding;

    // SDK 33 (TIRAMISU) 부터 필요
//    private static final String NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS";
    private final String NOTIFICATION_REQUIRED_PERMISSION = android.Manifest.permission.POST_NOTIFICATIONS;
    private final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // API 33 (Tiramisu) 이상에서만 알림 권한을 체크합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission();
            initEvent();
        } else {
            // API 33 미만은 권한이 자동 부여되므로 바로 이벤트 초기화
            Log.d(TAG, "API 33 미만: 알림 권한 자동 부여 확인. 이벤트 초기화.");
            initEvent();
        }
    }


    /**
     * 버튼 이벤트 처리
     */
    private void initEvent(){

        Intent intent = new Intent(this, MusicForegroundService.class);

        binding.btnStartMusic.setOnClickListener( v -> {
            // 서비스 시작 전, 알림 권한을 다시 한번 확인 (사용자가 설정에서 껐을 경우 대비)
            if (isNotificationPermissionGranted()) {
                Log.i(TAG, "음악 서비스 시작 요청.");
                //TODO 1. 서비스가 포그라운드에서 실행되도록 요청해야 합니다.
                // 완성하세요
                ContextCompat.startForegroundService(MusicControllerActivity.this, intent);
                
            } else {
                Toast.makeText(this, "알림 권한이 없어 서비스를 시작할 수 없습니다. 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                checkAndRequestNotificationPermission(); //권한 요청 다이얼로그 재시도 (사용자에게 다시 요청)
            }
        });

        binding.btnStopMusic.setOnClickListener( v -> {
            Log.i(TAG, "음악 서비스 정지 요청.");
            //TODO 2. 포그라운드 서비스를 정지합니다.
            // 완성하세요
            stopService(intent);
        });
    }

    //--------------Notification 권한 관련  START--------------------------------------------
    /**
     * Notification Step 1-1. SDK 33 (TIRAMISU) 이상에서 알림 권한을 확인하고 요청합니다.
     * onRequestPermissionsResult()메서드가 자동호출 됩니다
     */
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, NOTIFICATION_REQUIRED_PERMISSION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // 권한 요청
                ActivityCompat.requestPermissions(
                        this, new String[]{NOTIFICATION_REQUIRED_PERMISSION}, NOTIFICATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * Notification Step 1-2.알림 권한 획득 성공/실패 여부를 알리는 call back method
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MusicControllerActivity.this, "알림 권한 획득 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MusicControllerActivity.this, "알림 권한 획득 실패! 알림이 표시되지 않을 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 사용자가 설정에서 껐을 경우 대비해서 알림 권한을 다시 한번 확인
     * @return
     */
    private boolean isNotificationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, NOTIFICATION_REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 영구 거부 시 설정 화면으로 이동을 요청하는 다이얼로그를 표시합니다.
     */
    private void showSettingsDialog(){
        new AlertDialog.Builder(this)
                .setTitle("권한 필수 확인")
                .setMessage("이 서비스를 이용하려면 알림 권한이 필수입니다. 앱 설정 화면으로 이동하여 권한을 수동으로 허용해주세요.")
                .setPositiveButton("설정으로 이동", (dialogInterface, i) -> {
                    // 권한 설정 화면으로 이동
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("닫기", (dialogInterface, which) -> {
                    Toast.makeText(this, "알림 권한 없이는 서비스를 시작할 수 없습니다.", Toast.LENGTH_SHORT).show();
                })
                .create()
                .show();
    }
    //--------------Notification 권한 관련  END--------------------------------------------


}