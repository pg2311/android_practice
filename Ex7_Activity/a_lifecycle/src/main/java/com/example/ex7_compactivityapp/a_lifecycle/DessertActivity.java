package com.example.ex7_compactivityapp.a_lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Activity가 처음부터 시작되면 다음 세 가지 수명 주기 콜백이 순서대로 모두 호출됩니다
 *
 * onCreate(): 시스템이 앱을 생성할 때 호출
 * onStart(): 앱이 화면에 표시되도록 하지만 사용자는 아직 앱과 상호 작용 할 수 없습니다
 * onResume(): 앱을 포그라운드로 가져오고 사용자는 이제 앱과 상호 작용 할 수 있습니다
 * 이름과 달리 onResume() 메서드는 다시 시작할 대상이 없어도 시작 시 호출됩니다
 */
public class DessertActivity extends AppCompatActivity {
    private static final String TAG = "DessertActivityty";

    private int dessertCount;

    /**
     *
     * @param savedInstanceState Activity가 재생성될 때 이전 상태를 복원하는 데 도움을 줍니다. 예를 들어, 화면 회전이나 홈 버튼을 눌렀다가 다시 돌아오는 경우, 시스템은 현재 Activity의 상태를 저장하고 이를 savedInstanceState라는 Bundle 객체에 담아 전달합니다. 이 Bundle에는 사용자가 입력한 데이터나 UI 상태와 같은 정보를 저장할 수 있습니다. 개발자는 이를 활용해 Activity가 다시 생성될 때 필요한 데이터를 복원할 수 있습니다.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_dessert);
        TextView tvCount = findViewById(R.id.tvCount); //클릭된 디저트 개수용 TextView

        /*
         *화면 전환시 유지 2. 유지할 데이터값 얻기
         */
        if (savedInstanceState != null) {
            dessertCount = savedInstanceState.getInt("dessertCount", 0); // 기본값 0
        }
        tvCount.setText(dessertCount+"개");


        /*
         *디저트를 클릭 할 때
         */
        ImageView dessert = findViewById(R.id.imageDessert);
        dessert.setOnClickListener(view ->{
            dessertCount++;
            tvCount.setText(dessertCount+"개");
        });

        /*
         * 공유버튼 클릭 할 때
         */
        ImageButton shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText("디저트 " + dessertCount + "개");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dessert), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Intent를 만듭니다
     * Action : Intent.ACTION_SEND
     * Action에서 사용될 Data : text,  DataType : text/plain
     */
    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text); //데이터를 Intent.EXTRA_TEXT라는 표준 키를 사용하여 전달합니다
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }
    /*
     *화면 전환시 유지 1. 유지할 데이터값 설정하기
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt("dessertCount", dessertCount); // 정수값 저장
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, "onReStart");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


}