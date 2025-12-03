package com.example.a_touchevent;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
/**
 * 터치에 반응하여 고정된 위치에 단순한 삼각형을 그리는 사용자 정의 View 클래스입니다.
 * onTouchEvent()에서 invalidate()를 호출하여 onDraw()를 실행합니다.
 */
class CustomTriangleView extends View {

    // Canvas에 그리기 위한 Paint 객체
    private  Paint mPaint;
    // 그릴 경로를 정의하는 Path 객체
    private  Path mPath;

    // 터치되었는지 여부를 나타내는 플래그
    private boolean isTouched = false;


    // --- Constructors ---
    // XML에서 View를 사용할 수 있도록 세 가지 생성자를 모두 구현합니다.
    public CustomTriangleView(Context context) {
        super(context);
        init();
    }
    public CustomTriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomTriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // View 초기화 (Paint와 Path 설정)
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);        // 색상 설정 (단순한 빨간색)
        mPaint.setStyle(Paint.Style.FILL);// 스타일 설정: 도형을 채움
        mPaint.setAntiAlias(true); // 앤티앨리어싱 활성화
        mPath = new Path();

        // View에서 그림자를 사용하려면 이 코드를 호출해야 합니다. (그림자를 사용하지 않으므로 제거)
        // setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * View를 다시 그릴 때 호출되는 메서드입니다.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // isTouched가 true일 때만 그립니다.
        if (isTouched) {
            // 1. Path 초기화 및 정의 (고정된 위치의 삼각형)
            mPath.reset(); // 경로 재설정

            // X: 100~300, Y: 100~300 영역에 삼각형을 그립니다. (이전 요청의 좌표를 활용하여 고정)
            mPath.moveTo(200f, 100f);        // 시작점 (상단 꼭짓점)
            mPath.lineTo(300f, 300f);        // 오른쪽 아래 꼭짓점
            mPath.lineTo(100f, 300f);        // 왼쪽 아래 꼭짓점
            mPath.close();                   // 시작점으로 돌아가 닫음 (삼각형 완성)

            // 2. Canvas에 Path 그리기
            canvas.drawPath(mPath, mPaint);
        }
    }

    /**
     * 터치 이벤트가 발생했을 때 호출되는 메서드입니다.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 터치 좌표 업데이트 코드는 고정된 위치에 그리므로 필요 없습니다.
        // touchX = event.getX();
        // touchY = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("TRIANGLE", "DOWN");
                isTouched = true; // 터치 시작: 플래그를 true로 설정
                invalidate(); // View를 다시 그리도록 요청 -> onDraw() 호출
                // 터치 이벤트를 소비하고 후속 이벤트(UP)를 받겠다고 알립니다.
                return true;

            case MotionEvent.ACTION_UP:
                Log.i("TRIANGLE", "UP");
                isTouched = false;  // 터치 종료: 플래그를 false로 설정
                invalidate(); // View를 다시 그리도록 요청 -> onDraw() 호출 (삼각형이 사라집니다)
                return true; // 이벤트를 소비했으므로 true 반환
        }

        return super.onTouchEvent(event); // 처리하지 않은 다른 이벤트는 기본 동작을 따릅니다.
    }
}
public class PathTriangleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_path_triangle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}