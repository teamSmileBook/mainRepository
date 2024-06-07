package com.example.smilebook.search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smilebook.bookList.GridAdapter;
import com.example.smilebook.bookList.GridBookListData;
import com.example.smilebook.LoginActivity;
import com.example.smilebook.MainActivity;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.R;
import com.example.smilebook.admin.UserAdminModeSwitch;
import com.example.smilebook.alarm.UserAlarm;
import com.example.smilebook.UserMyInfo;

import java.util.List;

// 검색 결과 도서 목록을 표시함.
public class SearchResult extends AppCompatActivity {

    private static final String BASE_URL = "http://3.39.9.175:8080/";
    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // 검색 화면으로 이동
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchResult.this, SearchActivity.class));
                finish();
            }
        });

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 홈 버튼 클릭 시 메인 화면으로 이동
        Button home_btn = (Button) findViewById(R.id.icons8_smile);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResult.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 검색어 표시
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        Intent intent = getIntent();
        String query = intent.getStringExtra("query");
        categoryTextView.setText("' "+query+" ' 의 검색 결과");

        // 리사이클러뷰 설정
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 검색 결과 데이터 가져오기
        List <GridBookListData> bookList = (List <GridBookListData>) getIntent().getSerializableExtra("bookList");

        // 어댑터 설정
        if (bookList != null) {
            //검색 결과가 비어있지 않으면 데이터 표시
            Log.d("SearchResult","검색 결과 불러오기 성공");
            gridAdapter = new GridAdapter(bookList, this);
            recyclerView.setAdapter(gridAdapter);
        } else {
            // 검색 결과가 비어있으면 오류 메시지 표시
            Log.e("SearchResult","검색 결과 불러오기 실패");
            Toast.makeText(SearchResult.this, "검색 결과를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    //툴바에 menu_toolbar 삽입
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    // 알림 화면으로 이동
                    startActivity(new Intent(SearchResult.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    // 내 정보 화면으로 이동
                    startActivity(new Intent(SearchResult.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    // 내 도서 화면으로 이동
                    startActivity(new Intent(SearchResult.this, MyBookList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    // 관리자 인증 화면으로 이동
                    startActivity(new Intent(SearchResult.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(SearchResult.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(SearchResult.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
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