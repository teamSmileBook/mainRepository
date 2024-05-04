package com.example.smilebook;

import static android.app.ProgressDialog.show;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserMyInfoBinding;
import com.example.smilebook.model.MemberDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserMyInfo extends AppCompatActivity {

    private EditText editNickname, editEmail, editPassword, editPhoneNumber;

    private Button editButton;
    private ApiService apiService;
    private UserMyInfoBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        UserMyInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.user_my_info);

        //more 클릭 이벤트 처리
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // EditText 초기화
        editNickname = findViewById(R.id.editNickname);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPhoneNumber = findViewById(R.id.editPhonenumber);
        editButton = findViewById(R.id.myInfo_modify_button);

        // 서버에서 memberId를 전송, 회원 정보 가져오기
        getMemberDetailsFromServer();

        // SharedPreferences에서 id 값을 불러옴
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("memberId", "");

        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // TextView의 text 설정
        binding.setTitleText("정보 수정");

        toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
//        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // main_b 화면으로 이동하는 인텐트 생성
//                Intent intent = new Intent(user_my_info.this, main_b.class);
//                startActivity(intent);
//                // 현재 액티비티 종료
//                finish();
//            }
//        });

        //뒤로가기(more.xml)
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 수정 버튼 클릭 시
       editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정된 정보를 가져옴
                String newNickname = editNickname.getText().toString();
                String newEmail = editEmail.getText().toString();
                String newPhoneNumber = editPhoneNumber.getText().toString();
                String newPassword = editPassword.getText().toString();

                // 수정된 정보를 서버로 전송
                updateMemberInfo(newNickname, newEmail, newPhoneNumber, newPassword);
            }
        });



    }

    private void getMemberDetailsFromServer() {
        // SharedPreferences에서 memberId 가져오기
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        // Retrofit을 사용하여 서버에 요청하여 회원 정보 가져오기
        ApiService apiService = RetrofitClient.getApiService();
        MemberDTO request = new MemberDTO();
        request.setMemberId(memberId);

        Call<MemberDTO> call = apiService.getMemberDetails(request);
        call.enqueue(new Callback<MemberDTO>() {
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MemberDTO memberDTO = response.body();
                    // 회원 정보를 EditText에 나타냄
                    editNickname.setText(memberDTO.getNickname());
                    editEmail.setText(memberDTO.getEmail());
                    editPhoneNumber.setText(memberDTO.getPhoneNumber());
                } else {
                    Toast.makeText(UserMyInfo.this, "정보 조회 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                Toast.makeText(UserMyInfo.this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMemberInfo(String newNickname, String newEmail, String newPhoneNumber, String newPassword) {
        // SharedPreferences에서 memberId 가져오기
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = prefs.getString("memberId", "");

        // Retrofit을 사용하여 서버에 요청하여 회원 정보 업데이트
        ApiService apiService = RetrofitClient.getApiService();
        MemberDTO request = new MemberDTO();
        request.setMemberId(memberId);
        request.setPassword(newPassword);
        request.setNickname(newNickname);
        request.setEmail(newEmail);
        request.setPhoneNumber(newPhoneNumber);

        Call<Void> call = apiService.updateMemberInfo(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserMyInfo.this, "회원 정보 수정 완료", Toast.LENGTH_SHORT).show();
                    // 업데이트 성공 시 변경된 회원 정보 가져옴
                    getMemberDetailsFromServer();
                } else {
                    Toast.makeText(UserMyInfo.this, "회원 정보 수정 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserMyInfo.this, "서버 연결 오류", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(UserMyInfo.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(UserMyInfo.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(UserMyInfo.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_wishBookBtn) {
                    startActivity(new Intent(UserMyInfo.this, WishListActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    startActivity(new Intent(UserMyInfo.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(UserMyInfo.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(UserMyInfo.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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



