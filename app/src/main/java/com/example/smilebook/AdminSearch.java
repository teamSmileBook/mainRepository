package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.AdminSearchBinding;
import com.example.smilebook.databinding.SearchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

public class AdminSearch extends AppCompatActivity{

    private Button[] buttons;
    private AdminSearchBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.admin_search);

        // TextView의 text 설정
        binding.setTitleText("검색");

        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
//        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // main_b 화면으로 이동하는 인텐트 생성
//                Intent intent = new Intent(search.this, main_b.class);
//                startActivity(intent);
//                // 현재 액티비티 종료
//                finish();
//            }
//        });

        //뒤로가기
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
                Intent intent = new Intent(AdminSearch.this, AdminMore.class);
                startActivity(intent);
            }
        });


        //버튼 누르면 다른 색으로 변하는 효과 주려고 넣었던 코드
//        buttons = new Button[12];
//        buttons[0] = findViewById(R.id.tag_best);
//        buttons[1] = findViewById(R.id.tag_all);
//        buttons[2] = findViewById(R.id.tag_cook);
//        buttons[3] = findViewById(R.id.tag_cartoon);
//        buttons[4] = findViewById(R.id.tag_child);
//        buttons[5] = findViewById(R.id.tag_sports);
//        buttons[6] = findViewById(R.id.tag_literature);
//        buttons[7] = findViewById(R.id.tag_humanities);
//        buttons[8] = findViewById(R.id.tag_IT);
//        buttons[9] = findViewById(R.id.tag_edu);
//        buttons[10] = findViewById(R.id.tag_travel);
//        buttons[11] = findViewById(R.id.tag_health);
//
//        for (int i = 0; i < buttons.length; i++)
//        {
//            buttons[i].setOnClickListener(this);
//        }

    }
}
