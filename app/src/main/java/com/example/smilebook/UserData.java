package com.example.smilebook;

import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserDataBinding;
import com.example.smilebook.model.SuspensionReasonDTO;
import com.example.smilebook.model.UserDataDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserData extends AppCompatActivity {

    private TextView userIdTextView, userCardNumberTextView, suspendedUserTextView, warningTextView;
    private Button userStopButton, userWarningButton;
    private EditText warningReasonEditText;
    private UserDataBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.user_data);
        binding.setTitleText("회원 정보");
        toolbarTitleBinding = binding.toolbar;

        userIdTextView = binding.userId;
        userCardNumberTextView = binding.userCardnumber;
        suspendedUserTextView = binding.suspendedUser;
        warningTextView = binding.warning;
        userStopButton = binding.userStopButton;
        userWarningButton = binding.userWarningButton;
        warningReasonEditText = binding.warningReason;

        // 이전 액티비티로부터 memberId를 받음
        String memberId = getIntent().getStringExtra("memberId");

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        //뒤로가기
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadUserData(memberId); // 회원 정보 초기 로딩

        userStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserStatus();
            }
        });

        userWarningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveUserWarning();
            }
        });
    }

    //사용자 조회 기능
    private void loadUserData(String memberId) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<UserDataDTO> call = apiService.getMemberInfo(memberId);
        call.enqueue(new Callback<UserDataDTO>() {
            @Override
            public void onResponse(Call<UserDataDTO> call, Response<UserDataDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDataDTO userDataDTO = response.body();
                    userIdTextView.setText(userDataDTO.getUserId());
                    userCardNumberTextView.setText(userDataDTO.getRfidCardId());

                    if ("이용 정지 대상자".equals(userDataDTO.getMemberStatus())) {
                        suspendedUserTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
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
    }

    //
    private void updateUserStatus() {
        String suspensionReasonText = warningReasonEditText.getText().toString();
        // 이전 액티비티로부터 memberId를 받음
        String memberId = getIntent().getStringExtra("memberId");

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
    }

    //회원 경고 기능
    private void giveUserWarning() {
        String memberId = getIntent().getStringExtra("memberId");
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
    }

    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more_admin, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.admin_registrationBtn) {
                    startActivity(new Intent(UserData.this, BookRegistration.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_userBtn) {
                    startActivity(new Intent(UserData.this, UserList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.admin_transformBtn) {
                    startActivity(new Intent(UserData.this, MainActivity.class));
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();

    }
}
