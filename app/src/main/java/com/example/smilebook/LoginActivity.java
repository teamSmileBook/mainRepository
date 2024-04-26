package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, joinButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Retrofit 객체 생성
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        usernameEditText = findViewById(R.id.input_id);
        passwordEditText = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.loginButton);
        joinButton = findViewById(R.id.joinButton); // Join 버튼 초기화

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                // SharedPreferences를 사용하여 username 값을 저장
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                editor.putString("username", username);
                editor.apply();

                login(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 버튼 클릭 시 이동
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // JoinActivity로 이동하는 Intent를 생성합니다.
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);

                // Intent를 사용하여 JoinActivity로 이동합니다.
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<Void> call = apiService.login(loginRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    //로그인 성공 시 메인으로 이동, 로그인 요청 버튼 숨김
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .putExtra("hideButton", true));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
