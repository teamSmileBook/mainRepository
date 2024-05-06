package com.example.smilebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.model.MemberDTO;
import com.example.smilebook.model.ResponseDTO;
import com.example.smilebook.model.SuspensionReasonDTO;
import com.example.smilebook.model.UserDataDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class user_data extends AppCompatActivity {

    private TextView userIdTextView, userCardNumberTextView, suspendedUserTextView, warningTextView;
    private Button userStopButton, userWarningButton;
    private EditText warningReasonEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);

        userIdTextView = findViewById(R.id.userId);
        userCardNumberTextView = findViewById(R.id.user_cardnumber);
        suspendedUserTextView = findViewById(R.id.suspended_user);
        warningTextView = findViewById(R.id.warning);
        userStopButton = findViewById(R.id.userStopButton);
        userWarningButton = findViewById(R.id.userWarningButton);
        warningReasonEditText = findViewById(R.id.warning_reason);

        ApiService apiService = RetrofitClient.getApiService();
        Call<UserDataDTO> call = apiService.getMemberInfo("test");

        //회원 정보 조회 기능
        call.enqueue(new Callback<UserDataDTO>() {
            @Override
            public void onResponse(Call<UserDataDTO> call, Response<UserDataDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDataDTO userDataDTO = response.body();
                    userIdTextView.setText(userDataDTO.getUserId());
                    userCardNumberTextView.setText(userDataDTO.getRfidCardId());

                    if ("이용 정지 대상자".equals(userDataDTO.getMemberStatus())) {
                        suspendedUserTextView.setTextColor(Color.RED);
                    }
                    suspendedUserTextView.setText(userDataDTO.getMemberStatus());

                    int warningCount = userDataDTO.getWarningCount();
                    String warningText = "<font color='red'>" + warningCount + "</font>";
                    warningTextView.setText(Html.fromHtml("경고 횟수:   " + warningText + "번"));

                    if (warningCount >= 3) {
                        Toast.makeText(getApplicationContext(), "경고 횟수 3번 - 해당 회원 정지 처리 됩니다.", Toast.LENGTH_SHORT).show();
                    }

                    if ("이용 정지 대상자".equals(userDataDTO.getMemberStatus())) {
                        userStopButton.setText("정지 취소");
                    } else {
                        userStopButton.setText("회원 정지");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDataDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }

        });

        //회원 정지 기능
        userStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suspensionReasonText = warningReasonEditText.getText().toString();
                String memberId = "test";

                SuspensionReasonDTO suspensionReasonDTO = new SuspensionReasonDTO(memberId, suspensionReasonText);

                ApiService apiService = RetrofitClient.getApiService();
                Call<Void> call = apiService.updateSuspensionReason(suspensionReasonDTO);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            if (userStopButton.getText().toString().equals("회원 정지")) {
                                Toast.makeText(getApplicationContext(), "회원 정지 완료", Toast.LENGTH_SHORT).show();
                                userStopButton.setText("정지 취소");
                            } else {
                                Toast.makeText(getApplicationContext(), "정지 취소 완료", Toast.LENGTH_SHORT).show();
                                userStopButton.setText("이용 정지");
                            }
                        } else {
                            if (userStopButton.getText().toString().equals("회원 정지")) {
                                Toast.makeText(getApplicationContext(), "회원 정지 실패", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "정지 취소 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                    }
                });

                //새로고침
                Intent intent = new Intent(user_data.this, user_data.class);
                startActivity(intent);
                finish();
            }

        });

        //회원 경고 기능
        userWarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberId = "test";
                String suspensionReason = warningReasonEditText.getText().toString().trim();

                SuspensionReasonDTO suspensionReasonDTO = new SuspensionReasonDTO(memberId, suspensionReason);

                ApiService apiService = RetrofitClient.getApiService();
                Call<Void> call = apiService.updateWarning(suspensionReasonDTO);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "회원 경고 부여 완료", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원 경고 부여 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
                    }
                });

                //새로고침
                Intent intent = new Intent(user_data.this, user_data.class);
                startActivity(intent);
                finish();
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
                    startActivity(new Intent(user_data.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(user_data.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(user_data.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(user_data.this, WishListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(user_data.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(user_data.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(user_data.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

// SharedPreferences를 사용하여 "memberId" 값을 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("memberId", null);

// memberId가 null이면 로그인 버튼 텍스트 설정
        MenuItem logOutMenuItem = popupMenu.getMenu().findItem(R.id.user_logOutBtn);
        if (memberId == null) {
            logOutMenuItem.setTitle("로그인");
        } else {
            logOutMenuItem.setTitle("로그아웃");
        }

        popupMenu.show();
    }
}
