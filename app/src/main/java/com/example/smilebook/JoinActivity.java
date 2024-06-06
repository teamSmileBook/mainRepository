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


//회원가입 액티비티: 사용자가 회원가입 정보를 입력하고 제출할 때, 이를 서버에 전송하여 회원가입을 처리
public class JoinActivity extends AppCompatActivity {
    private ApiService apiService; //ApiService 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        // Retrofit 객체 생성
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 회원가입 버튼에 클릭 리스너 설정
        findViewById(R.id.join_info_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //입력된 회원가입 정보 가져오기
                String nickname = ((EditText) findViewById(R.id.join_info_nickName)).getText().toString();
                String memberId = ((EditText) findViewById(R.id.join_info_id)).getText().toString();
                String password = ((EditText) findViewById(R.id.join_info_pw)).getText().toString();
                String password2 = ((EditText) findViewById(R.id.join_info_pw_confirm)).getText().toString();
                String email = ((EditText) findViewById(R.id.join_info_eMail)).getText().toString();
                String phoneNumber = ((EditText) findViewById(R.id.join_info_phoneNum)).getText().toString();

                //입력값 유효성 검사
                if (nickname.isEmpty() || memberId.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(password2)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 회원가입 정보를 담은 MemberDTO 객체 생성
                MemberDTO memberDTO = new MemberDTO(memberId, nickname, password, email, phoneNumber);

                // 서버에 회원가입 요청 전송
                if (apiService != null) {
                    apiService.join(memberDTO).enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ResponseDTO responseBody = response.body();

                                // 서버 응답 코드에 따른 처리
                                if (responseBody.getCode() == 200) {
                                    // 회원가입 성공
                                    Toast.makeText(getApplicationContext(), responseBody.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                // 회원가입 실패
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
                    // Retrofit 객체가 초기화되지 않았을 경우
                    Toast.makeText(getApplicationContext(), "Retrofit 객체가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

