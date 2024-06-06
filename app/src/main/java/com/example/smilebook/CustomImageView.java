package com.example.smilebook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

//도서 위치를 표시하는 뷰: AppCompatImageView를 상속하여 커스텀 이미지 뷰를 생성
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

    //초기화 메소드
    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED); //원 색상 설정
        paint.setStyle(Paint.Style.FILL); //원 채우기 스타일 설정
    }

    //동그라미의 위치 설정
    public void setCirclePosition(float x, float y) {
        this.circleX = x;
        this.circleY = y;
        invalidate(); // 뷰를 다시 그리도록 요청
    }

    //뷰 생성 메소드
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //동그라미 생성
        canvas.drawCircle(circleX, circleY, radius, paint); // 동그라미 그리기
    }
}