package com.example.mp3player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MusicForegroundService extends Service {
    public MusicForegroundService() {
        Log.i(TAG, "Service 생성");
    }

    private static final String TAG = "MusicForegroundService";
    //채널ID : 앱이 생성하는 모든 알림은 반드시 하나의 채널에 할당되어야 합니다.
    //해당 채널에 속하는 모든 알림의 기본 동작 방식 (소리, 진동, 중요도 등)을 결정합니다
    private static final String CHANNEL_ID = "NotificationMusic";
    private static final String CHANNEL_NAME = "Music";

    //알림ID : 알림 창에 표시되는 개개의 메시지를 식별합니다.
    //알림 업데이트: 동일한 알림 ID로 다시 notify()를 호출하면 기존 알림을 새로운 내용으로 덮어씁니다
    //알림 취소: 이 ID를 사용하여 특정 알림을 제거할 수 있습니다.
    private static final int NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;

    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     * onStartCommand()를 통해 받은 Intent 내부의 데이터를 잃으면 안 되는 중요한 작업을 처리 중이라면,
     *  START_REDELIVER_INTENT를 반환한다.
     * 서비스의 상태 유지가 중요하고, 마지막 Intent가 손실되어도 괜찮을 때는 START_STICKY를 반환한다.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "음악 재생 시작!", Toast.LENGTH_SHORT).show();

        if (player != null) {
            player.stop();
            player.release();
        }
        int songResourceId =R.raw.song;
        player = MediaPlayer.create(this, songResourceId);
        if (player != null) {
            //TODO 1. 음악을 시작하고 ForegroundService용 알림을 표시(startForeground()합니다
            player.setLooping(true);
            player.start();

            // Notification Step 6. 알림 표시
            // FGS를 사용할 때: startForeground()를 호출하는 것만으로 알림이 띄워집니다. notify()는 알림의 내용을 변경할 때만 사용합니다.
            Notification notification = buildNotification("현재 음악이 재생 중입니다...");
            // TODO 이곳을 완성하세요
            startForeground(NOTIFICATION_ID, notification);

        } else {
            Log.e(TAG, "MediaPlayer 초기화 실패:"+ songResourceId +"리소스를 찾을 수 없습니다.");
        }
        return super.onStartCommand(intent, flags, startId);// return START_STICKY;과 같음

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "음악 정지!",
                Toast.LENGTH_SHORT).show();

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    /**
     * Notification Step 2. 알림 채널을 생성합니다. (API 26 이상에서 필수)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Channel Name : 안드로이드 기기에서 '설정' > '알림' > 해당 앱으로 이동하면, 앱에서 생성한 알림 채널 목록에 나타납니다
            //Channel Importance: 해당 채널에 속한 모든 알림이 사용자에게 얼마나 방해가 될지 그리고 어떻게 시각적/청각적으로 처리될지를 시스템에 알려주는 핵심 설정값
            //IMPORTANCE_HIGH : 소리, 진동, 헤드업 알림(Peek) -  발생. 화면이 켜진 상태에서 즉시 사용자에게 노출되어 가장 방해가 큼
            //IMPORTANCE_DEFAULT : 소리, 진동 발생. 알림 쉐이드에 표시되지만 헤드업 알림은 발생하지 않음
            //IMPORTANCE_LOW : 소리나 진동 없음. 알림 쉐이드에 표시되지만, 상태 표시줄 아이콘이 표시되지 않을 수도 있음
            //IMPORTANCE_MIN : 소리나 진동 없음. 알림 쉐이드에 아주 작게 표시

//            int importance = NotificationManager.IMPORTANCE_HIGH;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    importance
            );

            NotificationChannel existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (existingChannel == null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Notification STEP 3. PendingIntent준비
     * Notification STEP 4.Notification Builder 생성 및 설정
     * Notification STEP 5.Notification 객체 빌드
     *
     * 알림 객체 (Notification)를 빌드하여 반환합니다.
     * @param contentText 알림에 표시할 텍스트
     */
    private Notification buildNotification(String contentText) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();

        Intent intent = new Intent(this, MusicControllerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // PendingIntent.FLAG_IMMUTABLE은 SDK 23 이상에서 권장되며, SDK 31 이상에서 필수
        // 다른 앱이나 시스템 컴포넌트에게 전달되어도 내부의 Intent 객체를 수정할 수 없도록 만듭니다.
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE; //flags 변수에 추가적인 플래그 값을 설정
        }
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, flags);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        // TODO 2.  AutoCancel=false로 변경하여 포그라운드 서비스 알림이 사라지지 않도록 합니다
        //  완성하세요
        builder.setSmallIcon(R.drawable.ic_outline_play_arrow_24)
                .setContentTitle("MyMusic Foreground")
                .setContentText(contentText)

                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        return notification;
    }

}