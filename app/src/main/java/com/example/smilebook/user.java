package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

public class user extends AppCompatActivity {
    private View allUserView;
    private View stopUserView;
    private boolean  isAllUserVisible  = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        //검색 아이템 화면인텐트
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user.this, AdminSearch.class));
            }
        });

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });


        //버튼 및 뷰 초기화
        Button allUserBtn = findViewById(R.id.all_user_btn);
        Button stopUserBtn = findViewById(R.id.stop_user_btn);
        allUserView = findViewById(R.id.all_user_view);
        stopUserView = findViewById(R.id.stop_user_view);

        //전체회원 버튼에 클릭리스너 설정
        allUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAllUserVisible) {
                    allUserView.setVisibility(View.VISIBLE);
                    stopUserView.setVisibility(View.GONE);
                    isAllUserVisible = true;
                }
            }
        });

        //정지 회원 버튼에 클릭 리스너 설정
        stopUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAllUserVisible) {
                    allUserView.setVisibility(View.GONE);
                    stopUserView.setVisibility(View.VISIBLE);
                    isAllUserVisible = false;
                    stopUserView.setBackgroundResource(R.drawable.user_btn);
                }
            }
        });

    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(user.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(user.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(user.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(user.this, book_list.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(user.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // 로그아웃은 동작 해줘야함
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }


}
