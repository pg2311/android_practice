package com.scsa.a_smsreceiver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
 public class MainActivity extends AppCompatActivity implements SmsListener {
     private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final String[] PERMISSIONS = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS // 선택 사항: 읽기 권한도 함께 요청할 경우
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkAndRequestPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으므로 요청합니다.
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        } else {
            // 이미 권한이 부여됨
            onPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                onPermissionGranted();
            } else {
                onPermissionDenied();
            }
        }
    }

    private void onPermissionGranted() {
        String message = "SMS 수신 권한이 허용되었습니다. 이제 백그라운드에서 SMS를 수신할 수 있습니다. (메시지 수신 시 토스트가 표시됨)";
//        statusTextView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.i("MainActivity", "SMS 권한 허용됨.");
    }

    private void onPermissionDenied() {
        String message = "SMS 수신 권한이 거부되었습니다. 앱이 SMS를 수신할 수 없습니다.";
//        statusTextView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e("MainActivity", "SMS 권한 거부됨.");
    }

    private static SmsListener staticSmsListener;

    public static void setSMSListener(SmsListener listener) {
        staticSmsListener = listener;
    }

    public static SmsListener getSMSListener() {
        return staticSmsListener;
    }

     @Override
     public void onSmsReceived(String sms) {
         Log.i(TAG, "sms: " + sms);
         Toast.makeText(this, "sms: " + sms, Toast.LENGTH_LONG).show();
     }

     @Override
     public void onResume() {
         super.onResume();
         setSMSListener(this);
     }
 }