package com.example.smilebook;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class main_b extends AppCompatActivity {

    Toolbar mainToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_b);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar); //툴바 사용 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false); //타이틀 안 보이게
    }

    @Override

//    툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_toolbar, menu);
        return true;

    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.tool_search:
//                Intent intent = new Intent(getApplicationContext(), search.class);
//                startActivity(intent);
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

}