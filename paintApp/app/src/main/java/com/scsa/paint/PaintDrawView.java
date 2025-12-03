package com.scsa.paint;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Point {
    float x;
    float y;
    int color;
    int width;
    boolean isStart;
}

public class PaintDrawView extends View {
    private static final String TAG = "PaintDrawView";
    private Map<String, Integer> colorMap;
    private List<Point> points = new ArrayList<>();
    private int color;
    private int width;

    private void init(){
        colorMap = new HashMap<>();
        colorMap.put("RED", Color.RED);
        colorMap.put("BLACK", Color.BLACK);
        colorMap.put("BLUE", Color.BLUE);

        color = Color.BLACK;
        width = 10;
    }

    public PaintDrawView(Context context) {
        super(context);
        init();
    }

    public PaintDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setBrushColor(String color) {
        Log.i(TAG, "setBrushColor: " + color);
        if (colorMap.containsKey(color)) {
            this.color = colorMap.get(color);
        }
    }

    public void clearAll() {
        Log.i(TAG, "clearAll");
        points.clear();
        color = Color.BLACK;
        width = 10;
    }

    public void setBrushWidth(int w) {
        Log.i(TAG, "setBrushWidth: " + w);
        width = w;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (p.isStart) {
                continue;
            }

            Point p0 = points.get(i - 1);
            Paint paint = new Paint();
            paint.setColor(p.color);
            paint.setStrokeWidth(p.width);

            Log.i(TAG, "color: " + p0.color);

            canvas.drawLine(p0.x, p0.y, p.x, p.y, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point p = new Point();
        p.x = event.getX();
        p.y = event.getY();
        p.color = this.color;
        p.width = this.width;
        p.isStart = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: DOWN: x: " + p.x + ", y: " + p.y);
                points.add(p);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: MOVE: x: " + p.x + ", y: " + p.y);
                p.isStart = false;
                points.add(p);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: UP: x: " + p.x + ", y: " + p.y); p.isStart = false;
                p.isStart = false;
                points.add(p);
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
}
