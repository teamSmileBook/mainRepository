package com.example.smilebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserMore extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_more);

        Button closeBtn = (Button) findViewById(R.id.closeBtn);
        ImageButton user_alarm_btn = (ImageButton) findViewById(R.id.user_alarmBtn);
        ImageButton user_myInfo_btn = (ImageButton) findViewById(R.id.user_myInfoBtn);
        ImageButton user_admin_Trans_btn = (ImageButton) findViewById(R.id.user_adminTransBtn);
        ImageButton user_logout_btn = (ImageButton) findViewById(R.id.user_logOutBtn);
        ImageButton user_myBook_btn = findViewById(R.id.user_myBookBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });


        //알림
        user_alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarmIntent = new Intent(getApplicationContext(), UserAlarm.class);
                startActivity(alarmIntent);
            }
        });

        //내 정보
        user_myInfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInfoIntent = new Intent(getApplicationContext(), UserMyInfo.class);
                startActivity(myInfoIntent);
            }
        });

        //내 도서
        user_myBook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMore.this, user_book.class);
                startActivity(intent);
            }
        });

        //관리자 전환
        user_admin_Trans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminTransIntent = new Intent(getApplicationContext(), UserAdminModeSwitch.class);
                startActivity(adminTransIntent);
            }
        });

        user_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserMore.this);

                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(logoutIntent);
                    }
                });

                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });
    }
}
