package com.example.smilebook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageView extends AppCompatImageView {
    private Paint paint;
    private float circleX, circleY; // 동그라미의 중심 좌표
    private float radius = 18; // 동그라미의 반지름

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setCirclePosition(float x, float y) {
        this.circleX = x;
        this.circleY = y;
        invalidate(); // 뷰를 다시 그리도록 요청
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(circleX, circleY, radius, paint); // 동그라미 그리기
    }
}