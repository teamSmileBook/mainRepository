package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.SearchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

public class AdminSearch extends AppCompatActivity{

    private Button[] buttons;
    private SearchBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.search);

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
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
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

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminSearch.this, book_registration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(AdminSearch.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(AdminSearch.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }

}
