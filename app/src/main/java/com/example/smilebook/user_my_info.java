package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserAlarmBBinding;
import com.example.smilebook.databinding.UserMyInfoBinding;

public class user_my_info extends AppCompatActivity {

    private UserMyInfoBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        UserMyInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.user_my_info);

        // TextView의 text 설정
        binding.setTitleText("정보 수정");

        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // main_b 화면으로 이동하는 인텐트 생성
                Intent intent = new Intent(user_my_info.this, main_b.class);
                startActivity(intent);
                // 현재 액티비티 종료
                finish();
            }
        });

        //뒤로가기(more.xml)
        toolbarTitleBinding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more.xml
        toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_my_info.this, more.class);
                startActivity(intent);
            }
        });
    }
}
