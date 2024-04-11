package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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