package com.example.smilebook.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.alarm.UserAlarm;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.R;
import com.example.smilebook.UserMyInfo;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//관리자 모드 전환 액티비티: 입력된 코드를 서버로 전송해 조회 한 뒤 관리자 인증 처리
public class AdminModeSwitchActivity extends AppCompatActivity {

    private EditText adminCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_switch);

        adminCodeEditText = findViewById(R.id.admin_switch_code);

        // TextWatcher를 사용해 입력된 코드의 변화를 감지
        adminCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력 전 동작
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력 중 동작
            }

            @Override
            public void afterTextChanged(Editable s) {
                String adminCode = s.toString();
                // 입력된 코드가 4자리인지 확인
                if (adminCode.length() == 4) {
                    // API 호출해 관리자 코드 조회
                    checkAdminCode(adminCode);
                }
            }
        });



        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
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
                    startActivity(new Intent(AdminModeSwitchActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(AdminModeSwitchActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(AdminModeSwitchActivity.this, MyBookList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(AdminModeSwitchActivity.this, UserAdminModeSwitch.class));
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

    //관리자 코드 확인을 위한 api 호출 및 응답 처리
    private void checkAdminCode(String adminCode) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<String> call = apiService.checkAdminCode(adminCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String message = response.body();
                    Toast.makeText(AdminModeSwitchActivity.this, message, Toast.LENGTH_SHORT).show();
                    if ("코드 일치".equals(message)) {
                        // 코드 일치 시 AdminMainActivity로 전환
                        Intent intent = new Intent(AdminModeSwitchActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AdminModeSwitchActivity.this, "코드 불일치", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AdminModeSwitchActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}