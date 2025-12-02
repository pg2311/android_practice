package com.example.android.e_widget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

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
        ProgressBar progressBar = findViewById(R.id.progressBar);

//      작업 시작 시 ProgressBar 보이기
        progressBar.setVisibility(View.VISIBLE);

//        TODO 1. 10초 대기 후 progressBar 숨기기  ex) progressBar.setVisibility(View.GONE);
//        스레드의 이해
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
//                    progressBar.setVisibility(View.GONE);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                });
                Log.i("Thread", "새로운 스레드 입니다");
            }
        });
        t.start();
        Log.i("UI Thread", "기본 스레드 입니다");



        //progressBar2 style="?android:attr/progressBarStyleHorizontal"
        //작업의 진행 상황을 숫자로 표시
        ProgressBar progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setProgress(50); // 50% 진행

        //SeekBar : 슬라이더를 사용하여 값을 조정할 수 있는 위젯입니다. 사용자 터치로 임의 조절 가능합니다
        // (볼륨 조정, 밝기 조절 등)
        //대표속성 : android:max, android:progress, android:stepSize
        SeekBar seekBar = findViewById(R.id.seekBar);

        // SeekBar의 값 변경 리스너 설정
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress 값이 변경될 때마다 호출됨
                // 여기서 progress 값을 사용하여 작업 수행
                Log.i(TAG, "onProgressChanged() progress=" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 터치 시작할 때 호출됨
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 터치 종료할 때 호출됨
            }
        });

//        RatingBar : 사용자가 평가를 할 수 있도록 해주는 UI 컴포넌트입니다
        RatingBar ratingBar = findViewById(R.id.ratingBar);

//         RatingBar의 값 변경 리스너 설정
//         TODO 2. 별점값이 변경될 때 마다 로그로 현재 별점값을 출력
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.i(TAG, "onRatingChanged() rating=" + rating);
            }
        });

        /*
         SearchView의 텍스트 변화에 대한 리스너
         */
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setMaxWidth(Integer.MAX_VALUE);

//         TODO 3. Ex5_3UIAdapter 공부 후에!
//        데이터 ( originalList ) <- Adapter -> UI ( listView )
        ArrayList<String> originList = new ArrayList<>();
        originList.add("Apple");
        originList.add("Banana");
        originList.add("Cherry");

        ListView listView = findViewById(R.id.listView);

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, originList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 1. 입력할 때마다 호출 (실시간 필터링)
            @Override
            public boolean onQueryTextChange(String newText) {
                // ArrayAdapter의 내장 필터 기능을 사용하여 List의 내용을 필터링합니다.
                adapter.getFilter().filter(newText);
                return true; //이벤트를 처리했음을 알림
            }

            // 2. 엔터를 눌렀을 때
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 엔터 시 별도의 필터링은 하지 않고, 키보드만 숨기도록 합니다.
                // 1. 키보드 숨기기 로직 추가
                // 1-1. InputMethodManager 서비스 가져오기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 1-2. 현재 포커스를 가진 창에서 키보드를 숨기도록 지시
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                // 2. 포커스 해제 (SearchView에서 포커스 제거)
                searchView.clearFocus();

                Toast.makeText(MainActivity.this, "실시간 검색 후 엔터 처리", Toast.LENGTH_SHORT).show();
                return true; // 기본 동작(키보드 숨기기 등)을 시스템에 위임하지 않고 처리 완료 알림
            }
        });

//        webview를 사용하려면
//        AndroidManifest.xml에서
//        반드시 인터넷 사용 권한을 설정해야 합니다
//        <uses-permission android:name="android.permission.INTERNET"/>
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://news.samsung.com/kr/");



    }

}