package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.AdminModeSwitchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserMyInfoBinding;

public class admin_mode_switch extends AppCompatActivity {

    private ToolbarTitleBinding toolbarTitleBinding;
    private AdminModeSwitchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.admin_mode_switch);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.admin_mode_switch);

        // TextView의 text 설정
        binding.setTitleText("관리자 인증");

        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // main_b 화면으로 이동하는 인텐트 생성
                Intent intent = new Intent(admin_mode_switch.this, main_b.class);
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
                Intent intent = new Intent(admin_mode_switch.this, more.class);
                startActivity(intent);
            }
        });

    }
}
