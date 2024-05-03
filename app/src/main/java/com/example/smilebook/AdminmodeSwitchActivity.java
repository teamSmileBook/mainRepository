package com.example.smilebook;

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

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//관리자 모드 전환
public class AdminmodeSwitchActivity extends AppCompatActivity {

    private EditText adminCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_mode_switch);

        adminCodeEditText = findViewById(R.id.admin_switch_code);

        adminCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String adminCode = s.toString();
                // 입력된 코드가 4자리인지 확인
                if (adminCode.length() == 4) {
                    // API 호출
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
                    startActivity(new Intent(AdminmodeSwitchActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(AdminmodeSwitchActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(AdminmodeSwitchActivity.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(AdminmodeSwitchActivity.this, WishListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(AdminmodeSwitchActivity.this, UserAdminModeSwitch.class));
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

    //api 응답 처리
    private void checkAdminCode(String adminCode) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<String> call = apiService.checkAdminCode(adminCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String message = response.body();
                    Toast.makeText(AdminmodeSwitchActivity.this, message, Toast.LENGTH_SHORT).show();
                    if ("코드 일치".equals(message)) {
                        // 코드 일치 시 AdminMainActivity로 전환
                        Intent intent = new Intent(AdminmodeSwitchActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AdminmodeSwitchActivity.this, "코드 불일치", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AdminmodeSwitchActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}