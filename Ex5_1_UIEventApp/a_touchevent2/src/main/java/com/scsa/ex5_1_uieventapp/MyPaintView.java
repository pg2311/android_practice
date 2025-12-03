package com.scsa.ex5_1_uieventapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class Point {
    float x;
    float y;
    boolean isContinue;
}

public class MyPaintView extends View {

    private static final String TAG = "MyPaintView";
    private List<Point> list = new ArrayList<>();

    public MyPaintView(Context context) {
        super(context);
    }

    public MyPaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(20.0f);
        for (int i = 0; i < list.size(); i++) {
            Point p = list.get(i);
            if (p.isContinue) {  // 시작이 아닌 경우
                float startX = list.get(i - 1).x;
                float startY = list.get(i - 1).y;
                canvas.drawLine(startX, startY, p.x, p.y, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        MotionEvent.ACTION_DOWN : 화면 터치 시작
//        MotionEvent.ACTION_MOVE : 터치 이동
//        MotionEvent.ACTION_UP : 터치 끝

        float x = event.getX();
        float y = event.getY();
        Point p = new Point();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "화면터치시작 x=" + x + ", y=" + y);
                p.x = x;
                p.y = y;
                p.isContinue = false;
                list.add(p);
                invalidate();  // -> onDraw() 자동 호출
                return true;
            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "화면터치 이동 x=" + x + ", y=" + y);
                p.x = x;
                p.y = y;
                p.isContinue = true;
                list.add(p);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "화면터치 끝 x=" + x + ", y=" + y);
                p.x = x;
                p.y = y;
                p.isContinue = true;
                list.add(p);
                invalidate();
                return true;
            default:
                Log.i(TAG, "Event: " + event.getAction());
                break;
        }
        return super.onTouchEvent(event);
    }
}
