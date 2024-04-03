package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        // Retrofit 객체 생성
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        findViewById(R.id.rectangle_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = ((EditText) findViewById(R.id.join_info_nickName)).getText().toString();
                String memberId = ((EditText) findViewById(R.id.join_info_id)).getText().toString();
                String password = ((EditText) findViewById(R.id.join_info_pw)).getText().toString();
                String password2 = ((EditText) findViewById(R.id.join_info_pw_confirm)).getText().toString();
                String email = ((EditText) findViewById(R.id.join_info_eMail)).getText().toString();
                String phoneNumber = ((EditText) findViewById(R.id.join_info_phoneNum)).getText().toString();

                if (nickname.isEmpty() || memberId.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(password2)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                MemberDTO memberDTO = new MemberDTO(memberId, nickname, password, email, phoneNumber);
                //API 호출
                if (apiService != null) {
                    apiService.join(memberDTO).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ResponseDTO responseBody = response.body();

                                if (responseBody.getCode() == 200) {
                                    // 회원가입 성공
                                    Toast.makeText(getApplicationContext(), responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            // 통신 실패
                            Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Retrofit 객체가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

