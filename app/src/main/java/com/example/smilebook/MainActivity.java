package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        Button loginbtn = (Button) findViewById(R.id.rectangle_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //툴바 사용 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false); //타이틀 안 보이게

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override

//    툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.item_search) {
            Intent searchIntent = new Intent(getApplicationContext(), UserSearch.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.item_more) {
            Intent moreIntent = new Intent(getApplicationContext(), UserMore.class);
            startActivity(moreIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
//        Button loginbtn = (Button) findViewById(R.id.loginButton);
//        Button joinbtn = (Button) findViewById(R.id.joinButton);
//        loginbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //login.class 였는데 화면 연결 꼬여서 main_b.class로 연결해둠
//                //로그인 버튼 누르면 바로 main_b 화면으로 연결 되는 상황!
//                //추후 로그인 기능 넣으면서 화면 흐름 바꾸려면 여기 수정하기
//                Intent intent  = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        joinbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
//                startActivity(intent);
//            }
//        });
//
    }
}