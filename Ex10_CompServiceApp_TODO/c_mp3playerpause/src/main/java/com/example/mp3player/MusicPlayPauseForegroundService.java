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

import java.util.ArrayList;
import java.util.List;

/**
 * 플레이리스트를 만들어봅니다
 */
public class MusicPlayPauseForegroundService extends Service {

    private static final String TAG = "MusicForegroundService";
    private static final String CHANNEL_ID = "NotificationMusic";
    private static final String CHANNEL_NAME = "Music Playback Channel"; // 채널 이름 수정

    private static final int NOTIFICATION_ID = 101;


    private NotificationManager notificationManager;
    private MediaPlayer player;

    public static final String ACTION_START_RESUME = "com.example.mp3player.ACTION_START_RESUME";
    public static final String ACTION_PAUSE = "com.example.mp3player.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.example.mp3player.ACTION_STOP";

    //  (이전 곡, 다음 곡)
    public static final String ACTION_PREV = "com.example.mp3player.ACTION_PREV";
    public static final String ACTION_NEXT = "com.example.mp3player.ACTION_NEXT";

    // 포그라운드 상태 추적 변수
    private boolean isForeground = false;
    //---------------------------------
    // 재생 목록 관리 변수 추가 (R.raw.song, R.raw.jazzbyrima)
    private List<Integer> playlist = new ArrayList<>();
    private int currentTrackIndex = 0;

    // R.raw.song, R.raw.a의 이름을 알림에 표시하기 위한 맵 (선택 사항)
    private final String[] TRACK_NAMES = {"R.raw.song", "R.raw.jazzbyrima"};
    //---------------------------------

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Notification STEP 2. Notification Channel 생성 (한 번만)
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        Log.d(TAG, "Service onCreate: NotificationManager 초기화 및 채널 생성 완료.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 1. 인텐트와 액션이 유효한지 확인
        if (intent == null || intent.getAction() == null) {
            Log.w(TAG, "Intent is null or action is missing. Attempting START_RESUME.");
            // 시스템에 의해 재시작되었을 경우 기본 재생을 시도합니다.
            handleStartResume();
            return START_STICKY;
        }

        String action = intent.getAction();
        Log.d(TAG, "Received command action: " + action);

        // 2. 전달받은 액션에 따라 분기 처리
        switch (action) {
            case ACTION_START_RESUME:
                handleStartResume();
                break;
            case ACTION_PAUSE:
                handlePause();
                break;
            case ACTION_STOP:
                handleStop();
                break;
            //  ACTION_PREV 처리 추가
            case ACTION_PREV:
                handleSkipPrevious();
                break;
            //  ACTION_NEXT 처리 추가
            case ACTION_NEXT:
                handleSkipNext();
                break;
            default:
                Log.w(TAG, "Unknown action received: " + action);
                break;
        }

        return START_STICKY;
    }

    /**
     * 플레이어가 없으면 새로 만들고 재생 시작, 일시정지 상태라면 재개.
     */
    private void handleStartResume() {
        // 1. 플레이어가 완전히 정지된 상태 (null)
        if (player == null) {
            try {
                if (playlist.isEmpty()) {
                    playlist.add(R.raw.song);
                    playlist.add(R.raw.jazzbyrima);
                    currentTrackIndex = 0; // 초기 인덱스 설정
                }

                // 현재 인덱스의 곡을 재생 시작
                int songResourceId = playlist.get(currentTrackIndex);
                player = MediaPlayer.create(this, songResourceId);
                if (player != null) {
                    player.setLooping(true);
                    player.start();

                    String trackName = TRACK_NAMES[currentTrackIndex];
                    String contentText = "현재 음악 재생 중:" + trackName;
                    Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "MediaPlayer 새로 생성 및 재생 시작됨.");


                    // TODO 1. 최초 시작 시: startForeground() 호출
                    //FGS를 사용할 때: startForeground()를 호출하는 것만으로 알림이 띄워집니다.
                    // notify()는 알림의 내용을 변경할 때만 사용합니다.
                    Notification notification = buildNotification(contentText, false);
                    startForeground(NOTIFICATION_ID, notification);
                    isForeground = true; // 포그라운드 상태 플래그 설정
                    Log.d(TAG, "Foreground Service 시작 완료.");
                } else {
                    Log.e(TAG, "MediaPlayer 초기화 실패: " + TRACK_NAMES[currentTrackIndex] + " 리소스를 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                Log.e(TAG, "음악 재생 중 오류 발생", e);
            }
        }
        // 2. 플레이어가 존재하고 일시정지 상태에서 재개된 경우
        else if (!player.isPlaying()) {
            player.start();

            String trackName = TRACK_NAMES[currentTrackIndex];
            String contentText = "현재 음악 재생 중: " + trackName;
            Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "MediaPlayer 재생 재개됨.");

            // TODO 2. 이미 포그라운드 상태이므로: notify()로 알림만 업데이트합니다
            if (isForeground) {
                Notification notification = buildNotification(contentText, false);
                notificationManager.notify(NOTIFICATION_ID, notification);
                Log.d(TAG, "Foreground Notification 재생 상태로 업데이트 완료.");
            } else {
                // 드물지만, 플래그가 false인데 재개 명령이 온 경우 (강제 종료 후 재시작 등)
                Log.w(TAG, "재개 명령: isForeground 플래그가 false입니다. 서비스가 포그라운드에서 제거되었을 수 있습니다.");
            }
        }
    }

    private void handlePause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            String trackName = TRACK_NAMES[currentTrackIndex];
            String contentText = "음악이 일시정지되었습니다: " + trackName;
            Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "MediaPlayer 일시정지됨.");

            // 이미 포그라운드 상태이므로: notify()로 알림만 업데이트
            if (isForeground) {
                Notification notification = buildNotification(contentText, true);
                notificationManager.notify(NOTIFICATION_ID, notification);
                Log.d(TAG, "Foreground Notification 일시정지 상태로 업데이트 완료.");
            }
        } else {
            Toast.makeText(this, "재생 중인 음악이 없습니다.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "일시정지 요청: 재생 중인 플레이어가 없음.");
        }
    }

    private void handleStop() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        // TODO 4. 포그라운드 서비스 와 알림을 제거하고 서비스 종료
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();

        // 서비스 종료 시 플래그 업데이트
        isForeground = false;

        Toast.makeText(this, "음악 정지!", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service 및 MediaPlayer 종료 완료.");
    }
    //------------------

    /**
     * 이전 곡 재생 처리 로직을 구현합니다.
     */
    private void handleSkipPrevious() {
        if (playlist.isEmpty()) {
            Log.w(TAG, "이전 곡 요청: 재생 목록이 비어 있습니다.");
            Toast.makeText(this, "재생 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. 인덱스 업데이트 (이전 곡, 순환)
        // (현재 인덱스 - 1 + 목록 크기) % 목록 크기
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        Log.i(TAG, "이전 곡 건너뛰기: 새 인덱스 = " + currentTrackIndex);

        // 2. 새 곡 재생 및 알림 업데이트 헬퍼 호출
        startNewTrackAndNotify();
    }


    //----TODO Extra. 다음, 이전곡 재생 START---------------

    /**
     * 다음 곡 재생 처리 로직을 구현합니다.
     */
    private void handleSkipNext() {
        if (playlist.isEmpty()) {
            Log.w(TAG, "다음 곡 요청: 재생 목록이 비어 있습니다.");
            Toast.makeText(this, "재생 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. 인덱스 업데이트 (다음 곡, 순환)
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        Log.i(TAG, "다음 곡 건너뛰기: 새 인덱스 = " + currentTrackIndex);

        // 2. 새 곡 재생 및 알림 업데이트 헬퍼 호출
        startNewTrackAndNotify();
    }

    /**
     * 현재 인덱스의 새 곡을 재생하고 알림을 업데이트합니다.
     */
    private void startNewTrackAndNotify() {
        // 기존 플레이어 정리
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        try {
            int songResourceId = playlist.get(currentTrackIndex);
            player = MediaPlayer.create(this, songResourceId);

            if (player != null) {
                player.setLooping(true);
                player.start();

                String trackName = TRACK_NAMES[currentTrackIndex];
                String contentText = "현재 재생 중: " + trackName;
                Toast.makeText(this, contentText, Toast.LENGTH_SHORT).show();

                // 알림 업데이트
                if (isForeground) {
                    Notification notification = buildNotification(contentText, false);
                    notificationManager.notify(NOTIFICATION_ID, notification);
                } else {
                    // 재시작 시 포그라운드 전환 로직 (비상용)
                    Notification notification = buildNotification(contentText, false);
                    startForeground(NOTIFICATION_ID, notification);
                    isForeground = true;
                }
            } else {
                Log.e(TAG, "MediaPlayer 초기화 실패: 리소스 ID " + songResourceId);
            }
        } catch (Exception e) {
            Log.e(TAG, "음악 재생 중 오류 발생", e);
        }
    }
    //----TODO Extra. 다음, 이전곡 재생 END---------------


    /**
     * Notification STEP 2. 알림 채널을 생성합니다 (API 26 이상에서 필요).
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 음악 재생에는 IMPORTANCE_LOW (소리 없음) 또는 IMPORTANCE_DEFAULT (소리 있음)가 적절합니다.
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    importance
            );
            // 채널이 이미 존재하는지 확인하는 로직은 불필요. createNotificationChannel은 이미 존재하는 채널을 덮어씁니다.
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Notification STEP 3. PendingIntent준비
     * Notification STEP 4.Notification Builder 생성 및 설정
     * Notification STEP 5.Notification 객체 빌드
     * <p>
     * 알림 객체 (Notification)를 빌드하여 반환합니다.
     *
     * @param contentText 알림에 표시할 텍스트
     * @param isPaused    현재 재생이 일시정지 상태인지 여부
     */
    private Notification buildNotification(String contentText, boolean isPaused) {
        // Notification STEP 3. Activity로 돌아갈 PendingIntent 설정
        Intent intent = new Intent(this, MusicPlayPauseControllerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // Activity가 이미 실행 중이면 재사용

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, flags);

        //Notification STEP 4.  Notification Builder 생성 및 내용 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        int iconResource = isPaused ? R.drawable.ic_outline_play_arrow_24 : R.drawable.ic_outline_pause_arrow_24;


        builder.setSmallIcon(iconResource)
                .setContentTitle("MyMusic Foreground")
                .setContentText(contentText)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        //----TODO Extra. 미디어 스타일 추가 (선택 사항: 재생 컨트롤을 추가)  START---------------
        // --- 1. 재생 컨트롤 PendingIntent 설정 ---
        // 이전 곡/다음 곡 (생략 가능하나, 미디어 알림의 일반적인 구성 요소)
        PendingIntent prevPendingIntent = createPendingIntent(ACTION_PREV, 10);
        PendingIntent nextPendingIntent = createPendingIntent(ACTION_NEXT, 30);

        // 재생/일시정지 버튼 (isPaused에 따라 액션 결정)
        String playPauseAction = isPaused ? ACTION_START_RESUME : ACTION_PAUSE;
        int playPauseIcon = isPaused ? R.drawable.ic_outline_play_arrow_24 : R.drawable.ic_outline_pause_arrow_24;
        String playPauseText = isPaused ? "재생" : "일시정지";
        PendingIntent playPausePendingIntent = createPendingIntent(playPauseAction, 20);

        // --- 2. Action 객체 생성 및 Builder에 추가 (순서가 인덱스가 됨) ---
        // [인덱스 0]: 이전 곡
        builder.addAction(R.drawable.ic_outline_skip_previous_24, "이전 곡", prevPendingIntent);
        // [인덱스 1]: 재생/일시정지 (상태에 따라 아이콘/액션 변경)
        builder.addAction(playPauseIcon, playPauseText, playPausePendingIntent);
        // [인덱스 2]: 다음 곡
        builder.addAction(R.drawable.ic_outline_skip_next_24, "다음 곡", nextPendingIntent);

        // --- 3. MediaStyle 적용 ---
        androidx.media.app.NotificationCompat.MediaStyle mediaStyle =
                new androidx.media.app.NotificationCompat.MediaStyle();

        //축소된 뷰(Compact View)에 인덱스 0, 1, 2에 해당하는 버튼을 표시하도록 지정
        mediaStyle.setShowActionsInCompactView(0, 1, 2);
        builder.setStyle(mediaStyle);
        //----TODO Extra. 미디어 스타일 추가 (선택 사항: 재생 컨트롤을 추가)  END---------------

        //Notification STEP 5. Notification 객체 빌드
        return builder.build();
    }

    /**
     * Notification STEP 3.
     * PendingIntent를 생성하는 헬퍼 메서드 (Flags는 적절히 정의된 것을 사용해야 합니다)
     */
    private PendingIntent createPendingIntent(String action, int requestCode) {
        Intent intent = new Intent(this, MusicPlayPauseForegroundService.class);
        intent.setAction(action);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getService(this, requestCode, intent, flags);
    }

    /**
     * handleStop에서 stopSelf()를 호출하므로, 이 onDestroy는 서비스가 시스템에 의해 강제로 종료될 때만 호출됩니다.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service onDestroy (시스템 종료)");
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        isForeground = false;
    }
}