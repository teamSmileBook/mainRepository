package com.example.smilebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.MemberDTO;

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

                // 입력값 유효성 검사
                if (nickname.isEmpty() || memberId.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                MemberDTO memberDTO = new MemberDTO(memberId, nickname, password, password2, email, phoneNumber);

                // Retrofit을 통한 API 호출
                if (apiService != null) {
                    apiService.join(memberDTO).enqueue(new Callback<MemberDTO>() {
                        @Override
                        public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                            if (!response.isSuccessful()) {
                                // 서버 응답이 성공적이지 않은 경우
                                Toast.makeText(getApplicationContext(), "서버 응답이 실패했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // 서버 응답이 성공적인 경우, 정상적으로 처리합니다.
                            MemberDTO joinResponse = response.body();
                            if (joinResponse != null) {
                                Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                // 여기서 로그인 페이지로 이동하도록 구현
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // 응답이 비어 있거나 잘못된 형식인 경우
                                Toast.makeText(getApplicationContext(), "서버 응답이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MemberDTO> call, Throwable t) {
                            // 통신 실패
                            Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                            t.printStackTrace(); // 추가: 오류를 확인하기 위해 스택 트레이스 출력
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Retrofit 객체가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}