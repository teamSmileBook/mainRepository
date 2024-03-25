package com.example.smilebook;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.JoinRequest;
import com.example.smilebook.model.JoinResponse;

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
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        findViewById(R.id.rectangle_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = ((EditText) findViewById(R.id.join_info_nickName)).getText().toString();
                String memberId = ((EditText) findViewById(R.id.join_info_id)).getText().toString();
                String password = ((EditText) findViewById(R.id.join_info_pw)).getText().toString();
                String password2 = ((EditText) findViewById(R.id.join_info_pw_confirm)).getText().toString();
                String email = ((EditText) findViewById(R.id.join_info_eMail)).getText().toString();
                String phoneNumber = ((EditText) findViewById(R.id.join_info_phoneNum)).getText().toString();

                JoinRequest request = new JoinRequest(nickname, memberId, password, password2, email, phoneNumber);

                // Retrofit을 통한 API 호출
                apiService.join(request).enqueue(new Callback<JoinResponse>() {
                    @Override
                    public void onResponse(Call<JoinResponse> call, Response<JoinResponse> response) {
                        if (response.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                            // 여기서 로그인 페이지로 이동하도록 구현
                        } else {
                            // 회원가입 실패
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JoinResponse> call, Throwable t) {
                        // 통신 실패
                        Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
