package com.example.smilebook;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class book_location extends AppCompatActivity {

    CustomImageView imageView; // CustomImageView 객체 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_location);

        // CustomImageView 객체 초기화
        imageView = findViewById(R.id.imageView36);

        // 마킹 좌표 설정
        float circleX = 491; // 동그라미의 x 좌표
        float circleY = 194; // 동그라미의 y 좌표
        imageView.setCirclePosition(circleX, circleY);
    }

}
