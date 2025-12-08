package com.example.mp3player;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


/**
 * 데이터 통신 흐름 (IPC: Inter-Process Communication)
 * 두 컴포넌트는 서로 다른 프로세스에 존재할 수 있으며, 이들이 통신하는 과정은 다음과 같습니다.
 *
 * 요청: 요청자 앱의 코드가 ContentResolver 객체에 데이터를 요청합니다 (예: getContentResolver().query(uri, ...) ).
 *
 * 중개: ContentResolver는 요청에 포함된 URI를 분석하여, 해당 데이터를 소유한 ContentProvider를 시스템 내에서 찾습니다.
 *
 * 전달: ContentResolver는 이 요청을 ContentProvider로 **프로세스 간 통신(IPC)**을 통해 안전하게 전달합니다.
 *
 * 처리: ContentProvider는 요청을 받고, 자신의 내부 저장소에서 데이터를 조회하거나 변경하는 작업을 수행합니다.
 *
 * 응답: ContentProvider는 결과(Cursor나 URI)를 ContentResolver를 거쳐 요청자 앱으로 반환합니다.
 */
public class MusicPlayPauseContentActivity extends AppCompatActivity {

    private static final String TAG = "MusicControllerActivity";
    private com.example.mp3player.databinding.ActivityMusicControllerBinding binding;

    // SDK 33 (TIRAMISU) 부터 필요
//    private static final String NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS";
    private final String NOTIFICATION_REQUIRED_PERMISSION = android.Manifest.permission.POST_NOTIFICATIONS;
    private final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

    // TODO 1. 🔔 외부 저장소 읽기 권한
    private final int MEDIA_PERMISSION_REQUEST_CODE = 200;
    private final String READ_MEDIA_PERMISSION;

    public MusicPlayPauseContentActivity() {
        //TODO 2.
        // API 레벨에 따른 저장소 권한 결정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_PERMISSION = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            READ_MEDIA_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.mp3player.databinding.ActivityMusicControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO 3.  권한 확인 및 요청 (미디어 권한부터 먼저 확인)
        checkAndRequestMediaPermissions();



        // API 33 (Tiramisu) 이상에서만 알림 권한을 체크합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestNotificationPermission();

        } else {
            // API 33 미만은 권한이 자동 부여되므로 바로 이벤트 초기화
            Log.d(TAG, "API 33 미만: 알림 권한 자동 부여 확인. 이벤트 초기화.");

        }


        initEvent();
    }


    /**
     * TODO 6. ContentResolver를 사용하여 장치에 저장된 MP3 파일 URI 목록을 가져옵니다.
     * @return MP3 파일의 URI 문자열 목록
     */
    private ArrayList<String> fetchAudioFiles() {
        ArrayList<String> audioUris = new ArrayList<>();

        // MediaStore를 통해 외부 저장소의 오디오 파일을 쿼리
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] { MediaStore.Audio.Media.DATA };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        try (Cursor cursor = getContentResolver().query(collection, projection, selection, null, null)) {
            if (cursor != null) {
                int columnPath = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(columnPath);
                    Uri contentUri = Uri.parse("file://" + path); // 쿼리 결과 경로를 URI로 변환
                    audioUris.add(contentUri.toString());
                    Log.d(TAG, "찾은 오디오 파일: " + contentUri.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "오디오 파일 쿼리 오류: ", e);
            Toast.makeText(this, "오디오 파일을 불러오는 데 실패했습니다.", Toast.LENGTH_LONG).show();
        }

        if (audioUris.isEmpty()) {
            Toast.makeText(this, "장치에서 재생 가능한 MP3 파일을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
        }
        return audioUris;
    }

    /**
     * 서비스로 명령을 보내는 범용 메서드
     */
    private void sendCommandToService(String action) {
        // 미디어 권한이 없으면 실행하지 않음
        if (ContextCompat.checkSelfPermission(this, READ_MEDIA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "음악 재생을 위해 미디어 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            checkAndRequestMediaPermissions(); // 권한 재요청
            return;
        }

        ArrayList<String> playlistUris = fetchAudioFiles();
        if (playlistUris.isEmpty()) {
            return; // 재생 목록이 없으면 중단
        }

        Intent intent = new Intent(this, MusicPlayPauseContentForegroundService.class);
        intent.setAction(action);

        // 🔔 재생 목록 URI를 Intent에 담아 서비스로 전달
        intent.putStringArrayListExtra(MusicPlayPauseContentForegroundService.EXTRA_PLAYLIST, playlistUris);

        // 포그라운드 서비스 시작 (API 26+ 요구 사항)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent);
        } else {
            startService(intent);
        }
        Log.d(TAG, "Service 명령 전송: " + action);
    }
    /**
     * 버튼 이벤트 처리
     */
    private void initEvent(){
//        Intent intent = new Intent(this, MusicPlayPauseContentForegroundService.class);

        // TODO 주의: 각 버튼 마다 액션을 담은 새로운 Intent를 생성합니다.
        // 1. 시작/재개 버튼 리스너
        binding.btnStartMusic.setOnClickListener( v -> {
            // 서비스 시작 전, 알림 권한을 다시 한번 확인 (사용자가 설정에서 껐을 경우 대비)
            if (isNotificationPermissionGranted()) {
                Log.i(TAG, "음악 서비스 시작/재개 요청.");

                Intent startIntent = new Intent(this, MusicPlayPauseContentForegroundService.class);
                //시작 또는 재개 액션 설정
                startIntent.setAction(MusicPlayPauseContentForegroundService.ACTION_START_RESUME);

                //-------
                // 미디어 권한이 없으면 실행하지 않음
                if (ContextCompat.checkSelfPermission(this, READ_MEDIA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "음악 재생을 위해 미디어 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                    checkAndRequestMediaPermissions(); // 권한 재요청
                    return;
                }

                ArrayList<String> playlistUris = fetchAudioFiles();
                if (playlistUris.isEmpty()) {
                    return; // 재생 목록이 없으면 중단
                }



                // 🔔 재생 목록 URI를 Intent에 담아 서비스로 전달
                startIntent.putStringArrayListExtra(MusicPlayPauseContentForegroundService.EXTRA_PLAYLIST, playlistUris);
                //-------



                ContextCompat.startForegroundService(this, startIntent);
            } else {
                Toast.makeText(this, "알림 권한이 없어 서비스를 시작할 수 없습니다. 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                // 권한 요청 다이얼로그 재시도 (사용자에게 다시 요청)
                checkAndRequestNotificationPermission();
            }
        });

        // 2. 일시정지 버튼 리스너
        binding.btnPauseMusic.setOnClickListener( v -> {
            Log.i(TAG, "음악 서비스 일시정지 요청.");

            Intent pauseIntent = new Intent(this, MusicPlayPauseContentForegroundService.class);
            //TODO 2-1. 일시정지 액션 설정
            pauseIntent.setAction(MusicPlayPauseContentForegroundService.ACTION_PAUSE);

            //TODO 2-2. startService를 사용하여 이미 실행 중인 서비스로 명령 전달
            startService(pauseIntent);

            Toast.makeText(this, "음악 일시정지", Toast.LENGTH_SHORT).show();
        });

        // 3. 정지 버튼 리스너
        binding.btnStopMusic.setOnClickListener( v -> {
            Log.i(TAG, "음악 서비스 정지 요청.");

            Intent stopIntent = new Intent(this, MusicPlayPauseContentForegroundService.class);

            //TODO 3. 정지 액션 설정
            stopIntent.setAction(MusicPlayPauseContentForegroundService.ACTION_STOP);

            // 중지 액션은 stopService(intent);대신 서비스 내부에서 처리 후 stopSelf()를 호출하여 정상적으로 종료하는 것이 좋습니다.
            // stopService(intent);
            startService(stopIntent);
        });
    }




//--------------TODO 4.미디어 파일 접근 권한 확인 및 요청-----------
/**
 *  미디어 파일 접근 권한 확인 및 요청, onRequestPermissionsResult()에서 확인
 */
private void checkAndRequestMediaPermissions() {
    if (ContextCompat.checkSelfPermission(this, READ_MEDIA_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this,
                new String[]{READ_MEDIA_PERMISSION},
                MEDIA_PERMISSION_REQUEST_CODE);
    } else {
        // 권한이 이미 있다면 바로 서비스 시작 준비
        Log.d(TAG, "미디어 권한 이미 허용됨.");
    }
}

//--------------Notification 권한 관련  START--------------------------------------------
    /**
     * Notification STEP 1-1. SDK 33 (TIRAMISU) 이상에서 알림 권한을 확인하고 요청합니다.
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
     * Notification STEP 1-2.
     * 미디어권한,
     * 알림 권한 획득 성공/실패 여부를 알리는 call back method
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO 5. 미디어 권한 획득 성공/실패 여부확인
        if (requestCode == MEDIA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "미디어 권한 획득 성공! 음악 재생을 시작하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "미디어 권한 없이는 외부 음악 재생이 불가능합니다.", Toast.LENGTH_LONG).show();
                // 영구 거부 시 설정으로 유도하는 로직을 추가할 수 있음
            }
        }


        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MusicPlayPauseContentActivity.this, "알림 권한 획득 성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MusicPlayPauseContentActivity.this, "알림 권한 획득 실패! 알림이 표시되지 않을 수 있습니다.", Toast.LENGTH_LONG).show();
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