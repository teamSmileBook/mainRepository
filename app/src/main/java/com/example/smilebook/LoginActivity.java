package com.example.smilebook;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.LoginRequest;
import com.example.smilebook.model.TokenDTO;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//로그인 액티비티: 사용자가 아이디와 비밀번호를 입력하고 로그인 버튼을 클릭하면, 입력된 정보를 서버에 전송하여 로그인을 시도

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText; //아이디와 비밀번호 입력 필드
    private Button loginButton, joinButton;//로그인 및 회원가입 버튼
    private ApiService apiService;//apiService 객채

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        apiService = RetrofitClient.getInstance().create(ApiService.class);

        //아이디, 비밀번호 입력 필드 및 로그인/회원가입 버튼 초기화
        usernameEditText = findViewById(R.id.input_id);
        passwordEditText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.loginButton);
        joinButton = findViewById(R.id.joinButton);

        //로그인 버튼 클릭 리스너 생성
        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                //아이디를 SharedPreferences에 저장해 로그인 상태 유지
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("memberId", username);
                editor.apply();

                //로그인 메서드 호출
                login(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        //회원가입 버튼 클릭 리스너 생성
        joinButton.setOnClickListener(v -> {
            //JoinActivity로 이동하는 인텐트 설정
            Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(intent);
        });
    }

    //서버에 로그인 요청을 보내는 메서드
    private void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<Void> call = apiService.login(loginRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    sendTokenToServer();
                } else {
                    try {
                        //서버에서 받은 에러 메시지 처리
                        String errorBody = response.errorBody().string();
                        String cleanErrorBody = errorBody.replace("\"", "");
                        Toast.makeText(LoginActivity.this, cleanErrorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //통신 실패시 메시지 출력
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Firebase 토큰을 서버로 전송하는 메서드
    private void sendTokenToServer() {
        //ApiService 객체 생성
        ApiService apiService = RetrofitClient.getApiService();

        //sharedPreferences에서 아이디 가져오기
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        //Firebase 토큰 가져와 서버로 전송
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        if (!memberId.isEmpty()) {
                            TokenDTO tokenDTO = new TokenDTO(memberId, token);
                            Call<Void> call = apiService.sendFirebaseToken(tokenDTO);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Log.d(TAG, "Token sent successfully to server");
                                    } else {
                                        Log.e(TAG, "Failed to send token to server: " + response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e(TAG, "Failed to send token to server: " + t.getMessage());
                                }
                            });
                        }
                    } else {
                        Log.e(TAG, "Failed to get token: " + task.getException().getMessage());
                    }
                });
    }
}
