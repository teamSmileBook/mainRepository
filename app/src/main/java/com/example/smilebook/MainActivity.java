package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button loginbtn = (Button) findViewById(R.id.loginButton);
        Button joinbtn = (Button) findViewById(R.id.joinButton);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //login.class 였는데 화면 연결 꼬여서 main_b.class로 연결해둠
                //로그인 버튼 누르면 바로 main_b 화면으로 연결 되는 상황!
                //추후 로그인 기능 넣으면서 화면 흐름 바꾸려면 여기 수정하기
                Intent intent  = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

    }
}