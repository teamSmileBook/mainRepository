package com.example.smilebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smilebook.admin.UserAdminModeSwitch;
import com.example.smilebook.alarm.UserAlarm;
import com.example.smilebook.bookList.BookListActivity;
import com.example.smilebook.bookList.BookListAll;
import com.example.smilebook.myBook.MyBookList;
import com.example.smilebook.search.SearchActivity;
import com.example.smilebook.api.ApiService;
import com.example.smilebook.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 일반 사용자의 메인 액티비티 : 각 카테고리별 도서 목록을 보여주고, 검색 기능 등 사용자 메뉴들을 제공함
public class MainActivity extends AppCompatActivity {

    // 전체, 교육, 소설, 만화 카테고리별 도서 ImageView 변수 선언
    private ImageView allImageView1, allImageView2, allImageView3, allImageView4; //전체 ImageView
    private ImageView eduImageView1, eduImageView2, eduImageView3, eduImageView4; //교육 ImageView
    private ImageView ficImageView1, ficImageView2, ficImageView3, ficImageView4; //소설 ImageView
    private ImageView toonImageView1, toonImageView2, toonImageView3, toonImageView4; //만화 ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        // SharedPreferences를 사용하여 "memberId" 값을 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String memberId = sharedPreferences.getString("memberId", "");

        Button loginbtn = findViewById(R.id.rectangle_login);

        // memberId가 null 값이 아닐 시 로그인 요청 버튼 숨김
        if (!memberId.isEmpty()) {
            loginbtn.setVisibility(View.GONE);
        } else {
            loginbtn.setVisibility(View.VISIBLE);
        }

        // 검색 화면으로 이동하는 클릭 이벤트 처리
        findViewById(R.id.item_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        // 더보기 메뉴 클릭 시 팝업 메뉴 표시
        findViewById(R.id.item_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        // 메인 화면 카테고리별 도서 ImageView 초기화
        allImageView1 = findViewById(R.id.all_book_1);
        allImageView2 = findViewById(R.id.all_book_2);
        allImageView3 = findViewById(R.id.all_book_3);
        allImageView4 = findViewById(R.id.all_book_4);

        eduImageView1 = findViewById(R.id.edu_book_1);
        eduImageView2 = findViewById(R.id.edu_book_2);
        eduImageView3 = findViewById(R.id.edu_book_3);
        eduImageView4 = findViewById(R.id.edu_book_4);

        ficImageView1 = findViewById(R.id.fiction_book_1);
        ficImageView2 = findViewById(R.id.fiction_book_2);
        ficImageView3 = findViewById(R.id.fiction_book_3);
        ficImageView4 = findViewById(R.id.fiction_book_4);

        toonImageView1 = findViewById(R.id.cartoon_book_1);
        toonImageView2 = findViewById(R.id.cartoon_book_2);
        toonImageView3 = findViewById(R.id.cartoon_book_3);
        toonImageView4 = findViewById(R.id.cartoon_book_4);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // 교육 카테고리의 이미지 로드
        String eduImageName1 = "9791158742188.jpg";
        String eduImageName2 = "9788996100768.jpg";
        String eduImageName3 = "9788950968113.jpg";
        String eduImageName4 = "9788972995678.jpg";
        loadImage(eduImageName1, eduImageView1);
        loadImage(eduImageName2, eduImageView2);
        loadImage(eduImageName3, eduImageView3);
        loadImage(eduImageName4, eduImageView4);

        // 소설 카테고리의 이미지 로드
        String ficImageName1 = "9788998441012.jpg";
        String ficImageName2 = "9791130646381.jpg";
        String ficImageName3 = "9791161571188.jpg";
        String ficImageName4 = "9788937461033.jpg";
        loadImage(ficImageName1, ficImageView1);
        loadImage(ficImageName2, ficImageView2);
        loadImage(ficImageName3, ficImageView3);
        loadImage(ficImageName4, ficImageView4);

        // 만화 카테고리의 이미지 로드
        String toonImageName1 = "9791198527288.jpg";
        String toonImageName2 = "9791191884333.jpg";
        String toonImageName3 = "9791170626503.jpg";
        String toonImageName4 = "9791172034085.jpg";
        loadImage(toonImageName1, toonImageView1);
        loadImage(toonImageName2, toonImageView2);
        loadImage(toonImageName3, toonImageView3);
        loadImage(toonImageName4, toonImageView4);

        // 인기 도서 목록으로 이동하는 버튼 클릭 이벤트 처리
        Button bestArrow = (Button) findViewById(R.id.best_arrow_btn);
        bestArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListAll.class);
                intent.putExtra("category","인기 도서");
                startActivity(intent);
            }
        });

        // 교육 카테고리 도서 목록으로 이동하는 버튼 클릭 이벤트 처리
        Button eduArrow = (Button) findViewById(R.id.edu_arrow_btn);
        eduArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","교육");
                startActivity(intent);
            }
        });

        // 소설 카테고리 도서 목록으로 이동하는 버튼 클릭 이벤트 처리
        Button ficArrow = (Button) findViewById(R.id.fiction_arrow_btn);
        ficArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","소설");
                startActivity(intent);
            }
        });

        // 만화 카테고리 도서 목록으로 이동하는 버튼 클릭 이벤트 처리
        Button toonArrow = (Button) findViewById(R.id. cartoon_arrow_btn);
        toonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                intent.putExtra("category","만화");
                startActivity(intent);
            }
        });

    }

    // 서버로부터 도서 이미지를 불러오는 메서드
    public static void loadImage(String imageName, ImageView imageView) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 이미지 가져오는 API 호출
        Call<ResponseBody> call = apiService.getImage(imageName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 이미지 로드
                    Log.d("ImageLoader", "이미지 로드 응답 성공");
                    try {
                        // 이미지를 바이트 배열로 읽어옴
                        byte[] imageBytes = response.body().bytes();
                        // 바이트 배열로 Glide에 이미지 로드
                        Glide.with(imageView.getContext())
                                .load(imageBytes)
                                .into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 예외 처리
                        Log.e("ImageLoader", "이미지 로드 실패: " + e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ImageLoader", "서버 응답 실패");
            }
        });
    }

    // 상단의 메뉴바 팝업을 표시하는 메서드
    private void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_more, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user_alarmBtn) {
                    // 알림 화면으로 이동
                    startActivity(new Intent(MainActivity.this, UserAlarm.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myInfoBtn) {
                    // 내 정보 화면으로 이동
                    startActivity(new Intent(MainActivity.this, UserMyInfo.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_myBookBtn) {
                    // 내 도서 화면으로 이동
                    startActivity(new Intent(MainActivity.this, MyBookList.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_adminTransBtn) {
                    // 관리자 인증 화면으로 이동
                    startActivity(new Intent(MainActivity.this, UserAdminModeSwitch.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.user_logOutBtn) {
                    // SharedPreferences를 사용하여 "memberId" 값을 가져오기
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String memberId = sharedPreferences.getString("memberId", null);

                    if (memberId == null) {
                        // "로그인" 버튼을 눌렀을 때 로그인 액티비티로 이동
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        // SharedPreferences에서 "memberId" 값을 null로 변경
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("memberId", null);
                        editor.apply();

                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                        // MainActivity 새로고침
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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