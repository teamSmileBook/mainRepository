package com.example.smilebook.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.smilebook.LoginActivity;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.R;
import com.example.smilebook.admin.UserAdminModeSwitch;
import com.example.smilebook.UserMyInfo;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserAlarmBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 사용자의 알림을 표시하고 관리하는 화면을 제공
public class UserAlarm extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private ApiService apiService; // Retrofit을 사용하여 API와 통신하기 위한 ApiService 객체
    private UserAlarmBinding binding; // 데이터 바인딩을 위한 객체
    private ToolbarTitleBinding toolbarTitleBinding; // 툴바 데이터 바인딩을 위한 객체
    private RecyclerView recyclerView; // 알림 목록을 표시하기 위한 RecyclerView
    private AlarmAdapter adapter; // 알림 목록을 관리하기 위한 Adapter 객체
    private List<AlarmData> alarmList; // 알림 데이터를 저장하는 List 객체
    private SwitchCompat alarmSwitch; // 알림 목록을 표시하는 스위치

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.user_alarm);
        binding.setTitleText("알림");
        toolbarTitleBinding = binding.toolbar;

        // RecyclerView 설정
        recyclerView = findViewById(R.id.alarm_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 알림 목록 초기화
        alarmList = new ArrayList<>();
        adapter = new AlarmAdapter(this, alarmList); // Context 전달
        recyclerView.setAdapter(adapter);

        // SharedPreferences에서 알림 목록을 가져와서 표시
        fetchNotificationList();

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 뒤로가기 버튼 클릭 시 액티비티 종료
        toolbarTitleBinding.back.setOnClickListener(view -> finish());

        // 스위치 on/off에 대한 코드
        alarmSwitch = binding.pushAlarmSwitch; // 스위치 초기화
        alarmSwitch.setChecked(true); // 스위치 기본값 설정

         //스위치 리스너 설정
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            // 스위치 on/off에 대한 동작
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치가 true인 경우 리스트뷰를 보이게 함
                if (isChecked) {
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    // 스위치가 false인 경우 리스트뷰를 숨김
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    // SharedPreferences에서 알림 목록을 가져와서 표시하는 메서드
    private void fetchNotificationList() {
        SharedPreferences prefs = getSharedPreferences("FCM_PREFS", MODE_PRIVATE);
        String existingData = prefs.getString("notifications", "");

        try {
            JSONArray jsonArray = new JSONArray(existingData);

            alarmList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");

                AlarmData alarmData = new AlarmData();
                alarmData.setAlarm_text(title + ": " + body);
                alarmList.add(alarmData);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    // 알림 화면으로 이동
                    startActivity(new Intent(UserAlarm.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    // 내 정보 화면으로 이동
                    startActivity(new Intent(UserAlarm.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    // 내 도서 화면으로 이동
                    startActivity(new Intent(UserAlarm.this, MyBookList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    // 관리자 인증 화면으로 이동
                    startActivity(new Intent(UserAlarm.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(UserAlarm.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(UserAlarm.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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
