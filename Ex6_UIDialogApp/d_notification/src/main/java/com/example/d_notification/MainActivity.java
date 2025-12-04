package com.example.d_notification; // A 코드와 동일하게 패키지 이름 통일

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View; // A 코드와 유사하게 View 임포트
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat; // A 코드처럼 NotificationManagerCompat 사용
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //채널ID : 앱이 생성하는 모든 알림은 반드시 하나의 채널에 할당되어야 합니다.
    //해당 채널에 속하는 모든 알림의 기본 동작 방식 (소리, 진동, 중요도 등)을 결정합니다.
    private static final String CHANNEL_ID = "MyNotiChannelId"; // B 코드 유지

    //알림ID : 알림 창에 표시되는 개개의 메시지를 식별합니다.
    //알림 업데이트: 동일한 알림 ID로 다시 notify()를 호출하면 기존 알림을 새로운 내용으로 덮어씁니다
    //알림 취소: 이 ID를 사용하여 특정 알림을 제거할 수 있습니다.
    private static final int NOTIFICATION_ID = 100;
    private static final String CHANNEL_NAME = "Simple Notification"; // B 코드 유지

    private NotificationManager nm; // B 코드 유지
    // private boolean hasPermission = false; // A 코드에 없으므로 제거

    // SDK 33 (TIRAMISU) 부터 필요
    private final String REQUIRED_PERMISSION = android.Manifest.permission.POST_NOTIFICATIONS;
    private final int PERMISSION_REQUEST_CODE = 100; // A 코드와 이름 통일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 알림 채널 생성 (API 26 이상에서 필수)
        createNotificationChannel();

        // SDK 33 이상 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission(); // A 코드와 이름 통일
        }

        Button btnFire = findViewById(R.id.btn_fire);
        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // A 코드처럼 권한 확인 로직 없이 바로 알림 시도
                showNotification();
            }
        });
    }
    /**
     * 알림 채널을 생성합니다. (API 26 이상에서 필수)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel Name : 안드로이드 기기에서 '설정' > '알림' > 해당 앱으로 이동하면, 앱에서 생성한 알림 채널 목록이 나타납니다
            //Channel Importance: 해당 채널에 속한 모든 알림이 사용자에게 얼마나 방해가 될지 그리고 어떻게 시각적/청각적으로 처리될지를 시스템에 알려주는 핵심 설정값
            //IMPORTANCE_HIGH : 소리, 진동, 헤드업 알림(Peek) -  발생. 화면이 켜진 상태에서 즉시 사용자에게 노출되어 가장 방해가 큼
            //IMPORTANCE_DEFAULT : 소리, 진동 발생. 알림 쉐이드에 표시되지만 헤드업 알림은 발생하지 않음
            //IMPORTANCE_LOW : 소리나 진동 없음. 알림 쉐이드에 표시되지만, 상태 표시줄 아이콘이 표시되지 않을 수도 있음
            //IMPORTANCE_MIN : 소리나 진동 없음. 알림 쉐이드에 아주 작게 표시

            int importance = NotificationManager.IMPORTANCE_HIGH;
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    importance
            );

            NotificationChannel existingChannel = nm.getNotificationChannel(CHANNEL_ID);
            if(existingChannel == null){
                nm.createNotificationChannel(channel);
            }
        }
    }

    /**
     * SDK 33 (TIRAMISU) 이상에서 알림 권한을 확인하고 요청합니다.
     * (A 코드의 checkAndRequestNotificationPermission()과 동일)
     */
    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // 권한 요청
                ActivityCompat.requestPermissions(
                        this, new String[]{REQUIRED_PERMISSION}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) { // A 코드와 이름 통일
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "알림 권한 획득 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "알림 권한 획득 실패! 알림이 표시되지 않을 수 있습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 알림을 생성하고 표시합니다.
     */
    private void showNotification() {
        /* 3) PendingIntent준비: 알림 클릭 시 MainActivity를 다시 실행
        PendingIntent란? 일반적인 Intent는 즉시 또는 가까운 미래에 내 앱 내에서 실행됩니다. 하지만 PendingIntent는 내 앱의 프로세스가 살아있지 않더라도 (혹은 다른 앱의 프로세스 내에서) 특정 작업을 실행할 수 있도록 위임하는 것이 핵심 목적입니다.

사용자가 알림을 탭했을 때 실행할 작업을 정의하기 위해 Intent를 생성하고, 이를 PendingIntent로 만들어 알림에 연결합니다
       */
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // PendingIntent.FLAG_IMMUTABLE은 SDK 23 이상에서 권장되며, SDK 31 이상에서 필수
        // 다른 앱이나 시스템 컴포넌트에게 전달되어도 내부의 Intent 객체를 수정할 수 없도록 만듭니다.
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE; //flags 변수에 추가적인 플래그 값을 설정
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        //4) Notification Builder 생성 및 설정
//        NotificationCompat.Builder를 사용하여 알림의 내용을 구성합니다. 이때, 채널 ID를 사용합니다 (하위 버전 호환성 제공)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.error) // 필수: 작은 아이콘
                .setContentTitle("새로운 알림 제목") // 제목
                .setContentText("알림 내용입니다. 클릭하여 앱으로 돌아가세요.") // 내용
                .setPriority(NotificationCompat.PRIORITY_MIN) // API 25 이하의 중요도 설정
                .setContentIntent(pendingIntent) // 알림 클릭 시 실행될 인텐트 설정
                .setAutoCancel(true); // 사용자가 탭하면 알림이 자동으로 사라지도록 설정

        // 5) Notification 객체 빌드
        // NotificationCompat.Builder의 build()메서드를 호출하여 최종 Notification 객체를 생성합니다

        // 6) 알림 표시 (Notify)
        // NotificationManagerCompat (또는 NotificationManager)객체를 가져와 notify()메서드를 호출하여 알림을 사용자에게 표시합니다
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}