package com.example.ex11_storageapp.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex11_storageapp.R;


public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_VALUE = 0;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedpreference);


        tvCount = findViewById(R.id.tv_count);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        //저장소파일에서 count 키에 해당하는 값을 찾고 없다면 DEFAULT_VALUE값을 반환합니다
        int curValue = prefs.getInt("count", DEFAULT_VALUE);

        /*int curValue = getPreferences(MODE_PRIVATE)
                .getInt("count", DEFAULT_VALUE);*/



        /*getPreferences(MODE_PRIVATE)
                .edit()
                .putInt("count", ++curValue)
                .apply();*/

        updateTvCount(curValue);

        findViewById(R.id.btn_add_value).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curValue = prefs.getInt("count", DEFAULT_VALUE);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("count", ++curValue);
                editor.apply();

                updateTvCount(curValue);
            }
        });
        //TODO 1. count값 초기화
        findViewById(R.id.btn_init_value).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("count", DEFAULT_VALUE);
                editor.apply();
                updateTvCount(DEFAULT_VALUE);
            }
        });

        //TODO 2. sharedpreference파일 삭제
        findViewById(R.id.btn_remove).setOnClickListener(v ->{
            String prefsFileName = "sharedpreferences.MainActivity";
            boolean deleted = getApplicationContext().deleteSharedPreferences(prefsFileName);
            if (deleted) {
                Log.i("SharedPreferences", prefsFileName + ".xml 파일이 성공적으로 삭제되었습니다.");
            } else {
                Log.e("SharedPreferences", prefsFileName + ".xml 파일 삭제에 실패했거나 파일이 존재하지 않았습니다.");
            }
        });
    }

    private void updateTvCount(int value) {
        tvCount.setText("SharedPreferences Test: " + value);
    }
}