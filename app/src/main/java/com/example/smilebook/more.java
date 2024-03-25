package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class more extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_more);

        Button closeBtn = (Button) findViewById(R.id.closeBtn);
        ImageButton user_alarm_btn = (ImageButton) findViewById(R.id.user_alarmBtn);
        ImageButton user_myInfo_btn = (ImageButton) findViewById(R.id.user_myInfoBtn);
        ImageButton user_admin_Trans_btn = (ImageButton) findViewById(R.id.user_adminTransBtn);
        ImageButton user_logout_btn = (ImageButton) findViewById(R.id.user_logOutBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });


        //알림
        user_alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarmIntent = new Intent(getApplicationContext(), user_alarm_b.class);
                startActivity(alarmIntent);
            }
        });

        //내 정보
        user_myInfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInfoIntent = new Intent(getApplicationContext(), user_my_info.class);
                startActivity(myInfoIntent);
            }
        });

        //관리자 전환
        user_admin_Trans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminTransIntent = new Intent(getApplicationContext(), admin_mode_switch.class);
                startActivity(adminTransIntent);
            }
        });

        user_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent logoutIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(logoutIntent);
            }
        });
    }
}
