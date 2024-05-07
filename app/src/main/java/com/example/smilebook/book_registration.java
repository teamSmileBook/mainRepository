package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class book_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_registration);

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
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
                    startActivity(new Intent(book_registration.this, UserListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_modifyBtn) {
                    startActivity(new Intent(book_registration.this, AdminCodeEditActivity.class));
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
