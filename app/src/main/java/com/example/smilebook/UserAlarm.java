package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.smilebook.databinding.ToolbarTitleBinding;
import com.example.smilebook.databinding.UserAlarmBinding;

public class UserAlarm extends AppCompatActivity {
    private UserAlarmBinding binding;
    private ToolbarTitleBinding toolbarTitleBinding;
    private ListView listView;
    private AlarmListViewItemAdapter adapter;
    private SwitchCompat alarmSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터 바인딩 설정
        binding = DataBindingUtil.setContentView(this, R.layout.user_alarm);

        //item_more 클릭 이벤트 처리
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });


        // TextView의 text 설정
        //binding.setTitleText("알림");
        //toolbarTitleBinding = binding.toolbar;

        //홈(main_b.xml)으로
//        toolbarTitleBinding.icons8Smile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // main_b 화면으로 이동하는 인텐트 생성
//                Intent intent = new Intent(user_alarm_b.this, main_b.class);
//                startActivity(intent);
//                // 현재 액티비티 종료
//                finish();
//            }
//        });

        //뒤로가기
        toolbarTitleBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //더보기
        //toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //Intent intent = new Intent(UserAlarm.this, UserMore.class);
                //startActivity(intent);
            //}
        //});

        // 스위치 on/off에 대한 코드
        alarmSwitch = binding.pushAlarmSwitch; //스위치 초기화
        alarmSwitch.setChecked(true); //스위치 기본값 설정

        // 스위치 리스너 설정
//        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            //스위치 on/off에 대한 동작
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // 스위치가 true인 경우
//                if (isChecked) {
//                    // 리스트뷰를 보이게 함
//                    listView.setVisibility(View.VISIBLE);
//                } else {
//                    // 스위치가 false인 경우
//                    // 리스트뷰를 숨김
//                    listView.setVisibility(View.GONE);
//                }
//            }
//        });

        //알림 내용
        //listview 참조
        listView = binding.alarmList;
//        listView = findViewById(R.id.alarm_list);

        //adapter 초기화
        adapter = new AlarmListViewItemAdapter(this);

        //listview에 adapter 설정
        listView.setAdapter(adapter);
    }
    //상단에 있는 메뉴바
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    startActivity(new Intent(UserAlarm.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    startActivity(new Intent(UserAlarm.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    startActivity(new Intent(UserAlarm.this, user_book.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
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
