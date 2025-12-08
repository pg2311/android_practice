package com.example.mp3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;

/**
 * 헤드셋 및 블루투스 장치의 미디어 버튼(재생/일시정지/다음 곡/이전 곡) 이벤트를 수신합니다.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

    private static final String TAG = "MediaButtonReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Intent의 Action이 MEDIA_BUTTON이 아닌 경우 무시
        if (intent == null || !Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            return;
        }

        // 2. KeyEvent 추출 및 ACTION_DOWN (버튼 누름) 이벤트만 처리
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
            return;
        }

        // 3. 서비스로 보낼 Intent 준비
        Intent serviceIntent = new Intent(context, MusicPlayPauseForegroundService.class);
        String actionToSend = null;

        // 4. 키 코드에 따라 실행할 액션 결정
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                // 재생/일시정지 버튼은 토글 액션으로 처리
                actionToSend = MusicPlayPauseForegroundService.ACTION_TOGGLE_PLAY_PAUSE;
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                actionToSend = MusicPlayPauseForegroundService.ACTION_NEXT;
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                actionToSend = MusicPlayPauseForegroundService.ACTION_PREV;
                break;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                actionToSend = MusicPlayPauseForegroundService.ACTION_STOP;
                break;
        }

        // 5. 서비스 시작 (포그라운드 서비스 시작 권장)
        if (actionToSend != null) {
            serviceIntent.setAction(actionToSend);
            ContextCompat.startForegroundService(context, serviceIntent);
        }

        // 6. 브로드캐스트 이벤트 소비 (다른 미디어 앱이 이 이벤트를 받지 않도록)
        abortBroadcast();
    }
}