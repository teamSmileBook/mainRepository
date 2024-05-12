package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.BookRegistrationBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

public class book_registration extends AppCompatActivity {

    private BookRegistrationBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.book_registration);
        // TextView의 text 설정
        binding.setTitleText("도서 등록");
        toolbarTitleBinding = binding.toolbar;

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //뒤로가기
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    // 아무 동작 하지않음
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(book_registration.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(book_registration.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }
}
