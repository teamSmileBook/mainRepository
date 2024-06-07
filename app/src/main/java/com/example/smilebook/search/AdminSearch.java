package com.example.smilebook.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.bookManage.BookRegistration;
import com.example.smilebook.MainActivity;
import com.example.smilebook.R;
import com.example.smilebook.userManage.UserList;
import com.example.smilebook.databinding.SearchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

// 관리자가 도서 및 사용자를 검색하는 화면을 제공하는 액티비티
public class AdminSearch extends AppCompatActivity{

    private Button[] buttons; // 검색 옵션을 나타내는 버튼 배열
    private SearchBinding binding; // 데이터 바인딩을 위한 객체
    private ToolbarTitleBinding toolbarTitleBinding; // 툴바 타이틀 바인딩 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.search);
        binding.setTitleText("검색");
        toolbarTitleBinding = binding.toolbar;

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 뒤로가기 버튼 클릭 시 액티비티 종료
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                // 도서 등록 화면으로 이동
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(AdminSearch.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    // 사용자 목록 화면으로 이동
                    startActivity(new Intent(AdminSearch.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    // 메인 화면으로 이동
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
