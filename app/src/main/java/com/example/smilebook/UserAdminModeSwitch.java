package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.databinding.AdminModeSwitchBinding;
import com.example.smilebook.databinding.ToolbarTitleBinding;

public class UserAdminModeSwitch extends AppCompatActivity {

    private ToolbarTitleBinding toolbarTitleBinding;
    private AdminModeSwitchBinding binding;

    private EditText adminSwitchCodeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.admin_mode_switch);

        //툴바 관련 코드
        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.admin_mode_switch);

        // TextView의 text 설정
        binding.setTitleText("관리자 인증");

        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
//        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // main_b 화면으로 이동하는 인텐트 생성
//                Intent intent = new Intent(admin_mode_switch.this, main_b.class);
//                startActivity(intent);
//                // 현재 액티비티 종료
//                finish();
//            }
//        });

        //뒤로가기(more.xml)
        toolbarTitleBinding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });


        //숫자 4자리 입력 받고 엔터 눌렀을 때 관리자 화면으로 전환
        adminSwitchCodeEditText = findViewById(R.id.admin_switch_code);

        adminSwitchCodeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                //엔터키를 눌렀을 때
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    //입력된 코드가 4자리면
                    if (adminSwitchCodeEditText.getText().length() == 4) {
                        //화면이동
                        moveAdminMain();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    //admin_main.xml로 이동
    private void moveAdminMain() {
        Intent adminSwitchIntent = new Intent(UserAdminModeSwitch.this, AdminMainActivity.class);
        startActivity(adminSwitchIntent);
        finish(); // 현재 액티비티 종료
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(UserAdminModeSwitch.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(UserAdminModeSwitch.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(UserAdminModeSwitch.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(UserAdminModeSwitch.this, book_list.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(UserAdminModeSwitch.this, UserAdminModeSwitch.class));
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
