package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

        // TextView의 text 설정
        binding.setTitleText("알림");
        toolbarTitleBinding = binding.toolbar;

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
        toolbarTitleBinding.ReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //더보기
        toolbarTitleBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAlarm.this, UserMore.class);
                startActivity(intent);
            }
        });

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

        //adapter 참조
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "반납완료");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 7일 남았습니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "대출완료");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "반납완료");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 0일 남았습니다");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 1일 남았습니다");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 7일 남았습니다");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "대출완료");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "반납완료");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납기간까지 0일 남았습니다.");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 1일 남았습니다");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_yellow), "반납 기간까지 7일 남았습니다");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.circle_green), "대출완료");

        //listview에 adapter 설정
        listView.setAdapter(adapter);
    }
}
